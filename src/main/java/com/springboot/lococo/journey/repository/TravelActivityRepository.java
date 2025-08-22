package com.springboot.lococo.journey.repository;

import com.springboot.lococo.journey.model.TravelActivity;
import com.springboot.lococo.journey.model.TravelSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelActivityRepository extends JpaRepository<TravelActivity, Long> {
    List<TravelActivity> findBySchedule(TravelSchedule schedule);
}
