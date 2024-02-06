package com.coding.crab.api.mail;

import lombok.RequiredArgsConstructor;

/**
 * 회원가입 메일 인증시 인증코드를 보내는 주체
 */
@RequiredArgsConstructor
public enum MailSender {

    TEMP("djdj2297@gmail.com"); // TODO djdj2297@gmail.com은 임시로 사용 중. 새 Gmail 만들어서 그걸로 바꿔줄 예정

    public final String address;
}
