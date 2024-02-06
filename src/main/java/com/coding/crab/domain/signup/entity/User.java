package com.coding.crab.domain.signup.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter @Setter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;        // 이름
    private String password;    // 비밀번호 (비밀번호 재입력 - 이건 프론트단에서 체크)
    private String mail;        // 이메일
    private String nickname;    // 닉네임
    private String phoneNo;     // 핸드폰 번호
    private String address;     // 주소 (API 쓰기로 했었으니까)
}
