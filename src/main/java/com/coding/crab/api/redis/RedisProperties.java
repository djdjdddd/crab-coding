package com.coding.crab.api.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter // 반드시 필요(RedisConfiguration에서 이 RedisProperties의 필드(redisPort, redisHost) 값을 사용하므로)
@Configuration
public class RedisProperties {
    private int redisPort;
    private String redisHost;

    //     org.springframework.boot.autoconfigure.data.redis.RedisProperties : 얘는 Spring Data Redis 에서 만들어둔 RedisProperties 파일인가?? 안에 보니까 localhost, 6379 이런 거 다 정의돼있네
    public RedisProperties(
            @Value("${spring.data.redis.port}") int redisPort, // @Value 고도화 (by Tecoble ??)
            @Value("${spring.data.redis.host}") String redisHost){
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
}
