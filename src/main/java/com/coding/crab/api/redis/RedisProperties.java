package com.coding.crab.api.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Embedded Redis Server용 Properties
 */
@Getter
@Configuration
public class RedisProperties {
    private int redisPort;
    private String redisHost;

    public RedisProperties(
            @Value("${spring.data.redis.port}") int redisPort, // @Value 고도화 (by Tecoble ??)
            @Value("${spring.data.redis.host}") String redisHost){
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
}
