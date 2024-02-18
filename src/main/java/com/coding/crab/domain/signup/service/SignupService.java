package com.coding.crab.domain.signup.service;

import com.coding.crab.api.mail.MailAuthenticationMessage;
import com.coding.crab.api.mail.MailDTO;
import com.coding.crab.api.mail.MailService;
import com.coding.crab.api.redis.RedisRepository;
import com.coding.crab.api.redis.RedisService;
import com.coding.crab.common.UserType;
import com.coding.crab.domain.signup.entity.User;
import com.coding.crab.domain.signup.repository.UserRepository;
import com.coding.crab.domain.signup.request.SignupRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    // TODO. Service 와 ServiceImpl 구분하는 작업도 나중엔 필요할 듯.

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}") // 60초
    private long authCodeExpirationMillis;

    private final UserRepository userRepository;
    private final MailService mailService;
    private final RedisService redisService;        // (1) RedisTemplate 사용
    private final RedisRepository redisRepository;  // (2) CrudRepository 사용

    // TODO. 스프링 시큐리티 의존성 추가 후 PasswordEncoder 사용할 것.
    //private final PasswordEncoder passwordEncoder;

    /**
     * 인증 메일을 보냅니다.
     * @param mailDTO
     */
    public void sendAuthCodeMail(MailDTO mailDTO) {
        // 1. 메일 중복 체크
        checkDuplicatedMail(mailDTO.getMail());

        // 2. 인증코드 생성
        String authCode = createAuthCode();
        log.info("authCode = {}", authCode);
        mailDTO.setAuthenticationCode(authCode); // **테스트용 임시 setter
        MailDTO testDTO = mailDTO.setAuthCodeTest(mailDTO, authCode);

        // 3. 메일 전송
        mailService.sendMail(testDTO);

        // 4. 인증코드 Redis 에 저장
        // (1) RedisTemplate 사용한 버전
        redisService.setValues(AUTH_CODE_PREFIX + testDTO.getMail(), authCode, Duration.ofMillis(authCodeExpirationMillis));

        // (2) CrudRepository 사용한 버전
//        redisRepository.save(Authentication.of(authCode, mailDTO.getUserMail()));
    }

    /**
     * 메일 중복 체크
     * @param mail
     */
    private void checkDuplicatedMail(String mail) {
        Optional<User> user = userRepository.findByMail(mail);
        if (user.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", mail);

            // TODO. Exception 종류 바꿔야 함.
            // 유저의 데이터를 저장하기 전에 checkDuplicateEmail() 메소드를 호출해 같은 이메일을 사용하고 있는 다른 유저가 존재하는지 체크합니다.
            // 같은 이메일을 사용하는 유저가 존재할 경우 뒤에 나오는 커스텀 Exception 인 ValidationException 을 상속받는 DuplicateUserException 을 던지게 됩니다.
            throw new IllegalArgumentException("이미 가입한 메일입니다.");
        }
    }

    /**
     * 인증코드 생성
     * @return
     */
    private String createAuthCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            log.error("error", e);
//            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
            throw new RuntimeException(e);
        }
    }

    // 메일 인증 성공/실패 여부 체크
    public MailAuthenticationMessage checkAuthentication(SignupRequestDTO userRequestDTO){
        String authenticationCode = userRequestDTO.getAuthenticationCode();

        // (1) RedisTemplate 사용 -> 커스텀(e.g. RedisService)하기 좋아서 조건문도 클린하게 쓸 수 있어 좋음.
        Optional<String> mail = redisService.getValues(AUTH_CODE_PREFIX + userRequestDTO.getMail());
        // Optional 장점 이용
        if(mail.isPresent()){
            log.info("인증완료");
            return MailAuthenticationMessage.SUCCESS;
        }else{
            log.info("인증코드 잘못 입력했거나 유효기간 만료");
            return MailAuthenticationMessage.FAIL;
        }

        // (2) Repository 사용 -> **조건문이 뭔가 맘에 안듦.
//        Authentication find = redisRepository.findByAuthCode(authenticationCode);
//        if (find == null) {
//            log.info("인증코드 잘못 입력했거나 유효기간 만료");
//            return MailAuthenticationMessage.FAIL;
//        }else if(!find.getMail().equals(userRequestDTO.getMail())){
//            log.info("인증코드가 중복된 아주 아주 드문 경우");
//            return MailAuthenticationMessage.FAIL;
//        }else{
//            log.info("인증완료");
//            return MailAuthenticationMessage.SUCCESS;
//        }
    }

//    public MailVerificationResult verifiedCode(String mail, String authCode) {
//        checkDuplicatedMail(mail);
//        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + mail);
//        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
//
//        return MailVerificationResult.of(authResult);
//    }

    public void signup(SignupRequestDTO signupRequestDTO){
        // 1. toEntity 사용 : 공용 메서드 역할이라 조금 애매하다고 판단되어 사용 X
        //User user = signupRequestDTO.toEntity();

        // 2. 따로 build : 암호화 및 UserType.DEFAULT 등 추가 설정해줌
        User user = User.builder()
                .name(signupRequestDTO.getName())
                .mail(signupRequestDTO.getMail())
                .username(signupRequestDTO.getUsername()) // TODO. username(signUpRequest.getEmail()) ---> username 과 email 필드에 똑같이 email 정보를 넣어주는데 username 필드를 별도로 두는 이유는 소셜 로그인 때문입니다. Security 인증에서 인증 결과 정보를 저장하는 Authenticate 객체를 생성할 때 username 값은 필수로 요구 되는데 소셜 로그인에서는 경우에 따라 email 정보가 없을 수 있습니다. 이런 경우에는 email 값을 username 으로 사용하게 되면 username 값이 없기 때문에 인증이 불가능하게 됩니다. 그런데, 소셜 로그인의 경우에는 유저가 username 값을 직접 사용할 일은 없습니다. 이러한 점을 이용하여 일반 회원 가입의 경우에는 username 값으로 email 값을 사용하고 소셜 로그인의 경우에는 별도의 username 값을 생성하여 등록해주기 위해 username 필드를 별도로 사용하였습니다.
                .password(signupRequestDTO.getPassword()) // TODO. 암호화 필요 : passwordEncoder.encode
                .phoneNo(signupRequestDTO.getPhoneNo()) // TODO. 암호화 필요
                .address(signupRequestDTO.getAddress())
                .type(UserType.DEFAULT) // DEFAULT : 기본 회원가입을 통해 가입한 계정
                .build();
        
        User saveUser = userRepository.save(user); // **save 결과에 따른 처리가 더 필요할까..?
    }

    /*
    private void checkDuplicateEmail(String email) {
        if(userRepository.existsByEmail(email))
            throw new DuplicateUserException("사용중인 이메일 입니다.", new SimpleFieldError("email", "사용중인 이메일 입니다."));
    }
     */
}
