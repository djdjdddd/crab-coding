package com.coding.crab.api.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis에 저장, 조회, 삭제하는 메서드를 구현하는 클래스. RedisTemplate를 주입받아 Redis 데이터를 조작한다.
 * @author 김용희
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    // key와 data를 Redis에 저장한다. 만약 데이터에 만료 시간을 설정하고 싶다면 세 번째 파라미터로 Duration 객체를 전달한다.
    public  void setValues(String key, String data){
        ValueOperations values = stringRedisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration){
        ValueOperations values = stringRedisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    // key 파라미터로 받아 key를 기반으로 데이터를 조회한다.
    @Transactional(readOnly = true) // DB 트랜잭션에서 단순히 select(읽기)만 하는 경우에 readOnly = true 속성을 주면 성능에 조금이라도 이점을 얻을 수 있다고 한다.
    public String getValues(String key){
        ValueOperations values = stringRedisTemplate.opsForValue();
        if(values.get(key) == null){
            return "false"; // 이거 좀 맘에 안드는데
        }
        return (String) values.get(key);
    }

    // key를 파라미터로 받아 key를 기반으로 데이터를 삭제한다.
    public void deleteValues(String key){
        stringRedisTemplate.delete(key);
    }

    public void expireValues(String key, int timeout){
        stringRedisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    // 조회하려는 데이터가 없으면 “false”를 반환한다.
    public boolean checkExistsValue(String value){
        return !value.equals("false");
    }

    // 이건 어디다 쓰는 거지?
    public void setHashOps(String key, Map<String, String> data){
        HashOperations<String, Object, Object> values = stringRedisTemplate.opsForHash();
        values.putAll(key, data);
    }

    public String getHashOps(String key, String hashKey){
        HashOperations<String, Object, Object> values = stringRedisTemplate.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) stringRedisTemplate.opsForHash().get(key, hashKey) : "";
    }

    public void deleteHashOps(String key, String hashkey){
        HashOperations<String, Object, Object> values = stringRedisTemplate.opsForHash();
        values.delete(key, hashkey);
    }
}
