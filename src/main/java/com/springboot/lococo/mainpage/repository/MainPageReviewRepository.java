package com.springboot.lococo.user.mainpage.repository;

import com.springboot.lococo.user.mainpage.model.ReviewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MainPageReviewRepository extends JpaRepository<ReviewList, Long> {
    List<ReviewList> findTop10ByOrderByCreatedAtDesc();
    List<ReviewList> findByRatingGreaterThanEqualOrderByCreatedAtDesc(Integer rating);
}
