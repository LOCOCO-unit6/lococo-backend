package com.springboot.login_back.user.mypage.repository;

import com.springboot.login_back.user.mypage.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    // 이번 달 범위 내 여정 조회
    List<Journey> findByUserIdAndStartDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);


    // 진행중인 여정 최신순 조회 (기존)
    Optional<Journey> findFirstByStatusOrderByStartDateDesc(Journey.JourneyStatus status);
}
