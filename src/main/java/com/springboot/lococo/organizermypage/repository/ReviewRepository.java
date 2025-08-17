package com.springboot.lococo.organizermypage.repository;



import com.springboot.lococo.organizermypage.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAffiliationOrderByCreatedAtDesc(String affiliation);
}