package com.springboot.lococo.promotion.repository;

import com.springboot.lococo.promotion.model.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
}
