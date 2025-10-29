package com.springboot.lococo.journey.repository;

import com.springboot.lococo.journey.model.TravelSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

    // 사용자별 일정 조회
    List<TravelSchedule> findByUser_Id(Long userId);

    // 일정 타입별 조회
    List<TravelSchedule> findByScheduleType(TravelSchedule.ScheduleType scheduleType);

    // 진행 중인 여정 조회 (오늘 날짜 기준)
    @Query("SELECT ts FROM TravelSchedule ts WHERE ts.date >= :today ORDER BY ts.date ASC")
    List<TravelSchedule> findOngoingSchedules(@Param("today") LocalDate today);

    // 완료된 여정 조회 (오늘 날짜 기준)
    @Query("SELECT ts FROM TravelSchedule ts WHERE ts.date < :today ORDER BY ts.date DESC")
    List<TravelSchedule> findCompletedSchedules(@Param("today") LocalDate today);

    // 사용자별 진행 중인 여정 조회
    @Query("SELECT ts FROM TravelSchedule ts WHERE ts.user.id = :userId AND ts.date >= :today ORDER BY ts.date ASC")
    List<TravelSchedule> findOngoingSchedulesByUser(@Param("userId") Long userId, @Param("today") LocalDate today);

    // 사용자별 완료된 여정 조회
    @Query("SELECT ts FROM TravelSchedule ts WHERE ts.user.id = :userId AND ts.date < :today ORDER BY ts.date DESC")
    List<TravelSchedule> findCompletedSchedulesByUser(@Param("userId") Long userId, @Param("today") LocalDate today);
}
