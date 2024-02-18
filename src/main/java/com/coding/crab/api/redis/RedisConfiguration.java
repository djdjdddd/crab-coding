package com.coding.crab.api.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Embedded Redis Server용 Configuration
 */
@Configuration
// Annotation to activate Redis repositories. (공식문서 참조 : https://docs.spring.io/spring-data/redis/docs/current-SNAPSHOT/api/org/springframework/data/redis/repository/configuration/EnableRedisRepositories.html)
// @EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfiguration {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties){
        return new LettuceConnectionFactory(
                redisProperties.getRedisHost(),
                redisProperties.getRedisPort()
        );
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
