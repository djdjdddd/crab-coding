package com.coding.crab.domain.signup.repository;

import com.coding.crab.domain.signup.entity.User;
import com.coding.crab.domain.signup.request.SignupRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@Slf4j
@SpringBootTest
class SignupUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 저장")
    void saveUserTest(){
        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
                .name("김용희")
                .password("1")
                .mail("djdjdddd@khu.ac.kr")
                .nickname("쓰용")
                .phoneNo("01012345678")
                .address("동대문구")
                .build();

        // when
        User user = signupRequestDTO.toEntity();    // DTO to Entity
        User saveUser = userRepository.save(user);

        // then
        Assertions.assertThat(saveUser).isNotNull();
    }

    @Test
    @DisplayName("메일 중복 체크")
    void checkDuplicatedMail(){
        // given
        String mail = "test@test.com";

        // when
        Optional<User> findUser = userRepository.findByMail(mail);
        if(findUser.isPresent()){
            log.debug("UserServiceImpl.checkDuplicatedMail exception occur mail : {}", mail);
//            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
            throw new IllegalArgumentException("이미 가입한 메일입니다.");
        }

        // then
    }
}