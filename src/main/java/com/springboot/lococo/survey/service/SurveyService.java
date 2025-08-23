package com.springboot.lococo.survey.service;

import com.springboot.lococo.survey.dto.SurveyRequestDto;
import com.springboot.lococo.survey.model.UserSurvey;
import com.springboot.lococo.survey.repository.UserSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final UserSurveyRepository surveyRepository;

    // 설문 저장 또는 업데이트
    public UserSurvey saveSurvey(SurveyRequestDto dto, String username) {
        UserSurvey survey = surveyRepository.findByIdentification(dto.getIdentification())
                .orElse(new UserSurvey());

        survey.setUsername(username);
        survey.setIdentification(dto.getIdentification());
        survey.setStartDate(dto.getStartDate());
        survey.setEndDate(dto.getEndDate());
        survey.setLocation(dto.getLocation());
        survey.setAgeRange(dto.getAgeRange());
        survey.setTravelStyle(dto.getTravelStyle());
        survey.setDailyBudget(dto.getDailyBudget());
        survey.setNote(dto.getNote());
        survey.setKeywords(dto.getKeywords());
        survey.setCompanions(dto.getCompanions());
        survey.setMustVisit(dto.getMustVisit());
        survey.setImageHints(dto.getImageHints());

        return surveyRepository.save(survey);
    }

    // 기존 메서드: UserSurvey 반환
    public UserSurvey getSurvey(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    // ✅ 설문 ID로 SurveyRequestDto 반환
    public SurveyRequestDto getSurveyDtoById(Long id) {
        UserSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        SurveyRequestDto dto = new SurveyRequestDto();
        dto.setStartDate(survey.getStartDate());
        dto.setEndDate(survey.getEndDate());
        dto.setLocation(survey.getLocation());
        dto.setAgeRange(survey.getAgeRange());
        dto.setTravelStyle(survey.getTravelStyle());
        dto.setDailyBudget(survey.getDailyBudget());
        dto.setNote(survey.getNote());
        dto.setKeywords(survey.getKeywords());
        dto.setCompanions(survey.getCompanions());
        dto.setMustVisit(survey.getMustVisit());
        dto.setImageHints(survey.getImageHints());
        dto.setIdentification(survey.getIdentification());

        return dto;
    }
}

