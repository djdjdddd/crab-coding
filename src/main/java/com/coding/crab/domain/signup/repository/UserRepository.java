package com.coding.crab.domain.signup.repository;

import com.coding.crab.domain.signup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // CRUD랑 JPA 차이??
    List<User> findByName(String userName);
    Optional<User> findByMail(String mail);
}
