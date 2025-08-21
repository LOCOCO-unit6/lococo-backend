package com.springboot.lococo.journey.repository;

import com.springboot.lococo.journey.model.TravelActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelActivityRepository extends JpaRepository<TravelActivity, Long> {
    // 특정 일정(schedule) 기준 활동 목록 조회
    List<TravelActivity> findByScheduleId(Long scheduleId);
}
