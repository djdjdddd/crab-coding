package com.coding.crab.api.mail;

import jakarta.activation.CommandMap;
import jakarta.activation.MailcapCommandMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private static final String senderMail = "djdj2297@gmail.com";

    public void sendMail(MailDTO mailDTO){
        SimpleMailMessage message = createMailForm(mailDTO);

        javaMailSender.send(message);

        // try catch 왜 쓴 거지?
    }

    // 발신한 이메일 데이터 세팅
    private SimpleMailMessage createMailForm(MailDTO mailDTO){
        log.debug("mailDTO={}", mailDTO);

        //
//        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
//        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
//        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
//        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
//        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
//        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
//        CommandMap.setDefaultCommandMap(mc);

        //
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getUserMail());
        message.setFrom(senderMail);
        message.setSubject("Test : 회원가입 인증 코드");
        message.setText("회원님의 인증 코드는 : " + mailDTO.getAuthCode());

        // 왜 이걸 쓰면 java.io.IOException: No MimeMessage content 에러가 뜨는 거지..?
        // ★원인 : 파라미터가 null
        // 파라미터가 @Nullable 이라고 명시돼있는데, 왜 그러한 에러가..?
        /*
        public void setText(@Nullable String text) {
            this.text = text;
        }
         */
//        message.setText(mailDTO.getAuthCode());

        return message;
    }
}
