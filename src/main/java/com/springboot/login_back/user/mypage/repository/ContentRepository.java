package com.springboot.login_back.user.mypage.repository;

import com.springboot.login_back.user.mypage.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByType(String type);
    List<Content> findByLocationContaining(String location);
    List<Content> findByTitleContainingOrDescriptionContaining(String title, String description);
}
