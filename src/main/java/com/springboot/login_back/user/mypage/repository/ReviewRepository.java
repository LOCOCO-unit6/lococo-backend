package com.springboot.login_back.user.mypage.repository;


import com.springboot.login_back.user.mypage.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findTop10ByOrderByCreatedAtDesc();
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Review> findTop10ByRating(Integer rating);
}
