package com.coding.crab.api.mail;

import lombok.Getter;

@Getter
public enum MailAuthenticationMessage {
    SUCCESS(11), FAIL(22);

    private int number;

    MailAuthenticationMessage(int number){
        this.number = number;
    }
}
