package com.springboot.login_back.user.mypage.repository;

import com.springboot.login_back.user.mypage.model.UserFavoriteContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteContentRepository extends JpaRepository<UserFavoriteContent, Long> {
    List<UserFavoriteContent> findByUserId(Long userId);
}

