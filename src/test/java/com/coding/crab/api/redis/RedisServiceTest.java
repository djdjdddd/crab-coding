package com.coding.crab.api.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class RedisServiceTest {

    final String KEY = "key";
    final String VALUE = "value";
    final Duration DURATION = Duration.ofMillis(5000);

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setValueBeforeTest(){
        redisService.setValues(KEY, VALUE, DURATION);
    }

    @AfterEach
    void deleteValueAfterTest(){
        redisService.deleteValues(KEY);
    }

    @Test
    @DisplayName("Redis에 데이터를 저장하면 정상적으로 조회된다.")
    void saveAndFindTest(){
        // when
        String findValue = redisService.getValues(KEY);

        // then
        assertThat(VALUE).isEqualTo(findValue);

        log.debug("??");
    }
}
