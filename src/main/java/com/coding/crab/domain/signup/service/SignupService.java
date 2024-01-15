package com.coding.crab.domain.signup.service;

import com.coding.crab.api.mail.MailDTO;
import com.coding.crab.api.mail.MailService;
import com.coding.crab.api.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

//    private final MemberRepository memberRepository;

    private final MailService mailService;

    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendAuthCodeMail(MailDTO mailDTO) {
        // 0. 메일 중복 체크
//        this.checkDuplicatedEmail(toEmail);

        // 1. 인증코드 생성
//        String title = "Travel with me 이메일 인증 번호";
        String authCode = createCode();
        log.info("authCode={}", authCode);
        mailDTO.setAuthCode(authCode);

        // 2. 메일 전송
        mailService.sendMail(mailDTO);

        // 3. 인증코드 Redis에 저장
        // ★이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + mailDTO.getUserMail(),
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

//    private void checkDuplicatedEmail(String email) {
//        Optional<Member> member = memberRepository.findByEmail(email);
//        if (member.isPresent()) {
//            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
//            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
//        }
//    }

    private String createCode() {
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

//    public EmailVerificationResult verifiedCode(String email, String authCode) {
//        this.checkDuplicatedEmail(email);
//        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
//        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
//
//        return EmailVerificationResult.of(authResult);
//    }
}
