package com.springboot.lococo.repository;

import com.springboot.lococo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentification(String identification);
    Optional<User> findById(Long id);
    boolean existsByIdentification(String identification);
    boolean existsByEmail(String email);
}
