package com.springboot.lococo.user.mypage.repository;


import com.springboot.lococo.user.mypage.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTargetType(String targetType);
    List<Review> findByTargetNameContaining(String targetName);
    List<Review> findByRatingGreaterThanEqual(Integer rating);
}
