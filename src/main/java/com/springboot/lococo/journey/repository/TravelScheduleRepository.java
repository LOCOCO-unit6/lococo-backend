package com.springboot.lococo.journey.repository;

import com.springboot.lococo.journey.model.TravelSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

}
