package com.coding.crab.api.redis;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis에 저장, 조회, 삭제하는 메서드를 구현한 클래스. StringRedisTemplate를 주입받아 Redis 데이터를 조작한다.
 * @author 김용희
 * @See StringRedisTemplate
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

//    @Resource(name = "redisTemplate") // @Resource는 빈 이름을 통해 주입을 받는 어노테이션
//    private ValueOperations<String, String> valueOperations;

    /**
     * key, value를 Redis에 저장합니다.
     * @param key
     * @param value
     */
    public void setValues(String key, String value){
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * key, value, duration(만료시간)을 Redis에 저장합니다.
     * @param key
     * @param value
     * @param duration 만료시간
     */
    public void setValues(String key, String value, Duration duration){
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    /**
     * key를 기반으로 value를 조회합니다.
     * @param key
     * @return 데이터 존재시 value 리턴. 없을시 null 리턴
     */
    @Transactional(readOnly = true) // cf. DB 트랜잭션에서 단순히 select(읽기)만 하는 경우에 readOnly = true 속성을 주면 성능에 조금이라도 이점을 얻을 수 있다고 한다.
    public Optional<String> getValues(String key){
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String value = valueOperations.get(key);

        return Optional.ofNullable(value); // null일 수도 있기 때문에 Optional을 리턴
    }

    /**
     * key를 기반으로 values를 삭제합니다.
     * @param key
     * @return 삭제되었다면 true
     */
    public Boolean deleteValues(String key){
        return stringRedisTemplate.delete(key);
    }

    /**
     * 특정 key에 대한 TTL(Time to Live)을 설정합니다.
     * @param key @NonNull
     * @param timeout TTL(Time to Live, 유효기간)
     * @param unit @NonNull TTL에 대한 unit(e.g. TimeUnit.MILLISECONDS)을 지정
     * @return 
     */
    public Boolean expireValues(String key, int timeout, @NonNull TimeUnit unit){
        return stringRedisTemplate.expire(key, timeout, unit);
    }
}
