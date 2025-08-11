package com.springboot.login_back.repository;

import com.springboot.login_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentification(String Identification);
    boolean existsByIdentification(String identification);
}
