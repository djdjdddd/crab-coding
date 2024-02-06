package com.coding.crab.api.redis;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@ToString
// https://mingyum119.tistory.com/266
@RedisHash(value = "Authentication", timeToLive = 5) // value에 특정한 값을 넣어줌으로써, 데이터가 저장될 때 key의 prefix에 붙을 문자열이 정해질 수 있다. 또한, timeToLive 옵션을 통해 입력한 숫자만큼 초 단위로 유효기간을 지정할 수 있다.
public class Authentication {
    @Id // Redis 해시의 키로 사용되어, 객체를 고유하게 식별되는 것에 사용된다.
    private String id; // Spring Data Redis를 사용할 경우 @Id는 반드시 이것만 가능한 듯. (id 말고 mail을 @Id로 쓰려니까 에러 발생 : Entity com.coding.crab.api.redis.TestUser requires to have an explicit id field; Did you forget to provide one using @Id)

    //@Indexed // Secondary Indexes의 역할을 하도록 하는 역할. 필요 시 이 값으로 데이터를 조회하기 위해 인덱싱을 해주는 것이다.
    private String authCode;

    private String mail;

    // 정적 팩토리 메서드 : 객체 불변성을 지키고자 Setter 사용 X
    private Authentication(String authCode, String mail){
        this.authCode = authCode;
        this.mail = mail;
    }
    public static Authentication of(String authCode, String mail){
        return new Authentication(authCode, mail);
    }
}
