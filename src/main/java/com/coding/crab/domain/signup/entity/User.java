package com.coding.crab.domain.signup.entity;

import com.coding.crab.common.AuthorityType;
import com.coding.crab.common.BaseEntity;
import com.coding.crab.common.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_USER")
@Getter
@Setter // <------------ 나중에 지워야 함. (테스트 코드용)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String name;        // 이름

    @Column(unique = true)
    private String mail;

    @Column(nullable = false, unique = true)
    private String username;    // ID

    private String password;    // PW

    private String phoneNo;

    private String address;

    @Enumerated(EnumType.STRING)
    private UserType type;

    // 회원은 기본적으로 ROLE_USER 권한을 가지지만 추가적으로 권한을 더 가질 수 있기 때문에 List 타입으로 선언했습니다.
    // 그리고 권한 정보의 경우 독자적으로 조회되는 경우는 없기 때문에 별도의 엔티티로 구현하는 대신 ElementCollection 으로 처리하였습니다.
    @ElementCollection(targetClass = AuthorityType.class)
    @CollectionTable(name = "TBL_USER_AUTHORITY", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private List<AuthorityType> authorities = new ArrayList<>();

    @Builder
    public User(String name, String password, String mail, String username, String phoneNo, String address, UserType type){
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.username = username;
        this.phoneNo = phoneNo;
        this.address = address;
        this.authorities.add(AuthorityType.ROLE_USER); // 회원은 기본적으로 ROLE_USER 권한을 가지지만 추가적으로 권한을 더 가질 수 있기 때문에 List 타입으로 선언했습니다.... (중략)
        this.type = type;
    }

    // ** 스프링 시큐리티 의존성 추가하면 주석 해제할 부분. (import org.springframework.security.core.GrantedAuthority;)
    /*
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.toString())).collect(Collectors.toList());
    }
    */
}

