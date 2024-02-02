package com.coding.crab.domain.signup.controller;

import com.coding.crab.api.mail.MailDTO;
import com.coding.crab.api.mail.MailService;
import com.coding.crab.domain.signup.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입", description = "설명란")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final MailService mailService;

    private final SignupService signupService;

    @PostMapping("/mail-test")
    public void mailTest(){
        log.info("mail-test 요청 들어옴");

        MailDTO mailDTO = new MailDTO();
        mailDTO.setUserName("김용희");
//        mailDTO.setUserMail("jisu3148496@naver.com");
//        mailDTO.setUserMail("djdjdddd@khu.ac.kr");
//        mailDTO.setUserMail("hjmin1221@naver.com");

        //
        signupService.sendAuthCodeMail(mailDTO);
    }

}
