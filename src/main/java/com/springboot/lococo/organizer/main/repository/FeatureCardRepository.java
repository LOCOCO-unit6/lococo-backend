package com.springboot.lococo.organizer.main.repository;



import com.springboot.lococo.organizer.main.domain.FeatureCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureCardRepository extends JpaRepository<FeatureCard, Long> {}