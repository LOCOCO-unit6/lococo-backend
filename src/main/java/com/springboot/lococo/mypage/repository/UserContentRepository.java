package com.springboot.lococo.mypage.repository;

import com.springboot.lococo.mypage.model.UserMypageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserContentRepository extends JpaRepository<UserMypageContent, Long> {
    List<UserMypageContent> findByType(String type);
    List<UserMypageContent> findByLocationContaining(String location);
    List<UserMypageContent> findByTitleContainingOrDescriptionContaining(String title, String description);
}
