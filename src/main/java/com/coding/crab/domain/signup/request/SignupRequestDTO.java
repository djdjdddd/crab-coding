package com.coding.crab.domain.signup.request;

import com.coding.crab.domain.signup.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class SignupRequestDTO {
    @NotBlank
    private String name;                // 이름
    @NotBlank
    @Email
    private String mail;                // 이메일
    //@NotBlank
    private String nickname;            // 닉네임
    //@NotBlank
    private String password;            // 비밀번호
    //@NotBlank
    private String phoneNo;             // 핸드폰 번호
    //@NotBlank
    private String address;             // 주소
    private String authenticationCode;  // 메일 인증코드

    @Builder
    public SignupRequestDTO(String name, String password, String mail, String nickname, String phoneNo, String address){
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.nickname = nickname;
        this.phoneNo = phoneNo;
        this.address = address;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .password(password)
                .mail(mail)
                .nickname(nickname)
                .phoneNo(phoneNo)
                .address(address)
                .build();
    }
}
