package com.coding.crab.domain.signup.request;

import com.coding.crab.domain.signup.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequestDTO {

    @NotBlank
    @Size(max = 20, message = "너무 긴 이름입니다. (최대 20자)")
    private String name;                // 이름

    @NotBlank
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String mail;

    //@NotBlank
    private String username;            // ID

    // TODO. 개발 완료되면 {1,20} ---> {8,20} 으로 바꾸기
    @Pattern(regexp = "[a-zA-Z!@#$%^&*-_]{1,20}", message = "1~20 길이의 알파벳과 숫자, 특수문자만 사용할 수 있습니다.")
    private String password;            // PW

    //@NotBlank
    private String phoneNo;

    //@NotBlank
    private String address;

    private String authenticationCode;  // 메일 인증코드

    @Builder
    public SignupRequestDTO(String name, String password, String mail, String username, String phoneNo, String address){
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.username = username;
        this.phoneNo = phoneNo;
        this.address = address;
    }

    // TODO. 리팩토링 필요
    public User toEntity(){
        return User.builder()
                .name(name)
                .password(password)
                .mail(mail)
                .username(username)
                .phoneNo(phoneNo)
                .address(address)
                .build();
    }
}
