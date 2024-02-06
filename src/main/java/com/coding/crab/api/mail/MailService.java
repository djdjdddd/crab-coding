package com.coding.crab.api.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final MailSender MAIL_SENDER = MailSender.TEMP;
    
    // TODO 메일 인증 이력 남기는 로직 추가할 것.

    public void sendMail(MailDTO mailDTO){
        SimpleMailMessage message = createMailForm(mailDTO);

        // try catch 왜 쓴 거지?
        // -> MailSender 클래스의 send() 메서드를 보면 throws MailException 중임. 근데 얘가 RuntimeEx. 클라이언트 코드에서 예외 처리하라고 명시적으로 던지고 있어서 나도 예외처리 함.
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
//            throw new MailException() {}
// 습관적으로 throw new OOException 하려했는데.. 우선 추상클래스라 안됐고, 생각해보니까 javaMailSender의 디폴트 메서드인 send에서 던진 MailException을 굳이 또 던진다는 게 말이 안되긴 하더라고. 그래서 그냥 로그만 찍게 코드 짰음.
            log.error("error", e);
            log.error("errorMessage={}", e.getMessage());
        }
    }

    // 발신한 이메일 데이터 세팅
    private SimpleMailMessage createMailForm(MailDTO mailDTO){
        log.debug("mailDTO={}", mailDTO);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getMail()); // 인증메일 수신자
        message.setFrom(MAIL_SENDER.address); // 인증메일 발신자
        message.setSubject("Test : 회원가입 인증 코드"); // 인증메일 제목
        message.setText("회원님의 인증 코드는 " + mailDTO.getAuthenticationCode() + " 입니다."); // 인증메일 내용 (인증코드 포함)

        return message;

        // 왜 이걸 쓰면 java.io.IOException: No MimeMessage content 에러가 뜨는 거지..?
        // ★원인 : 파라미터가 null
        // 파라미터가 @Nullable 이라고 명시돼있는데, 왜 그러한 에러가..?
        /*
        public void setText(@Nullable String text) {
            this.text = text;
        }
         */
//        message.setText(mailDTO.getAuthCode());
    }
}
