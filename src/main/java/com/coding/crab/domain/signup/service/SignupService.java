package com.coding.crab.domain.signup.service;

import com.coding.crab.api.mail.MailAuthenticationMessage;
import com.coding.crab.api.mail.MailDTO;
import com.coding.crab.api.mail.MailService;
import com.coding.crab.api.redis.RedisRepository;
import com.coding.crab.api.redis.RedisService;
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

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    private final UserRepository userRepository;
    private final MailService mailService;
    private final RedisService redisService;        // (1) RedisTemplate 사용
    private final RedisRepository redisRepository;  // (2) CrudRepository 사용

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

        // 4. 인증코드 Redis에 저장
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
            throw new IllegalArgumentException("이미 가입한 메일입니다.");

            // throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
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

    public void register(SignupRequestDTO signupRequestDTO){
        User user = signupRequestDTO.toEntity();
        User saveUser = userRepository.save(user);
        // 분기 처리를 어떻게 하는게 좋을까..?
    }
}
