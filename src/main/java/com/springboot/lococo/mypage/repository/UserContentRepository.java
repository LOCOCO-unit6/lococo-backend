package com.springboot.lococo.mypage.repository;

import com.springboot.lococo.mypage.model.UserMypageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserContentRepository extends JpaRepository<UserMypageContent, Long> {

    @Query("SELECT c FROM UserMypageContent c WHERE c.favorite = true")
    List<UserMypageContent> findFavorites();
}
