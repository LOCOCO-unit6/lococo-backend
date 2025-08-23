package com.springboot.lococo.journey.dto;

import lombok.Data;

import java.util.List;

@Data
public class JourneySurveyRequestDto {
    private Long surveyId;
    private String location;           // 여행 장소
    private List<String> keywords;     // 관심 키워드
    private List<String> ageGroups;    // 연령대
    private String companion;          // 동반자 유형
    private String startDate;          // 시작 날짜
}
