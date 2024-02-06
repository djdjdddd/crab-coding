package com.coding.crab.api.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MailDTO {
    @NotBlank // @NotNull, @NotEmpty, @NotBlank 차이 : https://sanghye.tistory.com/36
    private String name;

    @NotBlank
    @Email
    private String mail;

    private String authenticationCode;    // 인증코드

    public MailDTO(String mail){
        this.mail = mail;
    }

    public MailDTO setAuthCodeTest(MailDTO mailDTO, String authenticationCode){
        this.authenticationCode = authenticationCode;
        return mailDTO;
    }
}
