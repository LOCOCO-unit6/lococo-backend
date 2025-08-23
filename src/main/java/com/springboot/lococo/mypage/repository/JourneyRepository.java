package com.springboot.lococo.mypage.repository;

import com.springboot.lococo.mypage.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    // 현재 진행 중인 단일 여정
    List<Journey> findByStartDateBeforeAndEndDateAfter(LocalDateTime now1, LocalDateTime now2);

    // 진행 중 여정 전체
    List<Journey> findByStartDateBeforeAndEndDateAfterOrderByStartDateAsc(LocalDateTime now1, LocalDateTime now2);

    // 지난 여정
    List<Journey> findByEndDateBeforeOrderByEndDateDesc(LocalDateTime now);

    // 상태로 조회
    List<Journey> findByStatus(Journey.Status status);
}
