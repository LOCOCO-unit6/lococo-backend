package com.springboot.lococo.survey.repository;

import com.springboot.lococo.survey.model.UserSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSurveyRepository extends JpaRepository<UserSurvey, Long> {
    Optional<UserSurvey> findByIdentification(String identification);
}
