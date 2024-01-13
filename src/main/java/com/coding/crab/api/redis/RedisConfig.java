package com.coding.crab.api.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis와의 연결 정보를 설정하고, Redis 데이터를 저장하고 조회하는 데 사용되는 RedisTemplate 객체를 생성하는 역할을 하는 클래스
 * @author 김용희
 */
@Configuration
@EnableRedisRepositories // Redis를 사용한다고 명시해주는 애노테이션
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties; // Redis 서버와의 연결 정보를 저장하는 객체이다. redis의 host와 port를 YAML 파일에서 수정할 수 있고 redisProperties.getHost(), redisProperties.getPort() 메서드를 통해 가져올 수 있다.

//    public RedisConfig(RedisProperties redisProperties){
//    public RedisConfig(){
//        this.redisProperties = new RedisProperties();
//    }

    // RedisProperties로 properties에 저장한 host, port를 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // LettuceConnectionFactory 객체를 생성하여 반환하는 메서드. 이 객체는 Redis Java 클라이언트 라이브러리인 Lettuce를 사용해서 Redis 서버와 연결해 준다.
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        // RedisTemplate 객체를 생성하여 반환한다. RedisTemplate은 Redis 데이터를 저장하고 조회하는 기능을 하는 클래스이다.
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // Redis 데이터를 직렬화하는 방식을 설정할 수 있다. Redis CLI를 사용해 Redis 데이터를 직접 조회할 때, Redis 데이터를 문자열로 반화해야하기 때문에 설정한다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;    }
}
