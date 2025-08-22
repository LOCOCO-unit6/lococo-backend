package com.springboot.lococo.userai.repository;

import com.springboot.lococo.userai.model.FestivalRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRecommendationRepository extends JpaRepository<FestivalRecommendation, Long> {
}
