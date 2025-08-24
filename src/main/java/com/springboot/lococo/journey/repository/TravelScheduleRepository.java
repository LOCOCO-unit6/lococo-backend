package com.springboot.lococo.journey.repository;

import com.springboot.lococo.journey.model.TravelSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

    // 오늘 날짜 이후(오늘 포함) 일정 조회 → 진행 중 일정
    List<TravelSchedule> findByDateGreaterThanEqualOrderByDateAsc(LocalDate today);

    // 오늘 날짜 이전 일정 조회 → 지난 일정
    List<TravelSchedule> findByDateBeforeOrderByDateDesc(LocalDate today);
}
