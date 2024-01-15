package com.coding.crab.api.mail;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MailDTO {

    private String userName;

    private String userMail;

    private String authCode;    // 인증코드
}
