package com.springboot.lococo.user.mypage.repository;

import com.springboot.lococo.user.mypage.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findByStatus(Journey.JourneyStatus status);
    Optional<Journey> findFirstByStatusOrderByStartDateDesc(Journey.JourneyStatus status);
    List<Journey> findByDestinationContaining(String destination);
}
