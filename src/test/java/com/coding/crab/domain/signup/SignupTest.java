package com.coding.crab.domain.signup;

import com.coding.crab.domain.signup.entity.User;
import com.coding.crab.domain.signup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class SignupTest {
    
    @Autowired
    private UserRepository repository;

    private User signupUser;

    @BeforeEach
    void before(){
        signupUser = User.builder()
                .name("김용희")
                .password("1234")
                .mail("djdjdddd@khu.ac.kr")
                .build();
    }

    @Test
    @DisplayName("findByUserName 테스트")
    @Transactional(readOnly = true) // 성능향상 (실제로 재보진 않음 ㅎ..)
    void findByUserName(){
        String name = signupUser.getName();
        List<User> findUserList = repository.findByName(name);

        log.info("findUserList={}", findUserList);
    }

    @Test
    @DisplayName("save 테스트")
    @Transactional
    void saveTest(){
        // given
        User signupUser = User.builder()
                .name("김용희")
                .password("1234")
                .mail("djdjdddd@khu.ac.kr")
                .build();

        // when
        User savedUser = repository.save(signupUser);
        log.info("signupUser={}", signupUser);
        log.info("savedUser={}", savedUser);

        // then
        assertThat(savedUser).isEqualTo(signupUser);
    }
    
    @Test
    @DisplayName("MySQL이 제대로 연결됐는지 테스트합니다.")
    void connectionTest(){
        long count = repository.count();
        log.info("count={}",count);
    }
}
