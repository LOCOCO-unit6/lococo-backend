package com.springboot.lococo.mypage.repository;


import com.springboot.lococo.mypage.model.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByTargetType(String targetType);
    List<UserReview> findByTargetNameContaining(String targetName);
    List<UserReview> findByRatingGreaterThanEqual(Integer rating);
}
