package com.springboot.lococo.mypage.repository;

import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.model.UserMypageContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserContentRepository extends JpaRepository<UserMypageContent, Long> {

    List<UserMypageContent> findByUserAndFavoriteTrueOrderByIdDesc(User user);

    Optional<UserMypageContent> findByUserAndContentId(User user, Long contentId);
}
