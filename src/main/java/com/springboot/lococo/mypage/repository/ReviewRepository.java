package com.springboot.lococo.mypage.repository;

import com.springboot.lococo.mypage.model.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<UserReview, Long> {
}
