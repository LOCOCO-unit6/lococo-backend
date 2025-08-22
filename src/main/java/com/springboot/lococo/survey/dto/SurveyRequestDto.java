package com.springboot.lococo.survey.dto;

import lombok.Data;
import java.util.List;

@Data
public class SurveyRequestDto {
    private String startDate;  // String으로 받음 (DB 저장 편의)
    private String endDate;
    private String location;
    private List<String> keywords;
    private String ageRange;
    private List<String> companions;
    private String travelStyle;
    private Integer dailyBudget;
    private List<String> mustVisit;
    private String note;
    private List<String> imageHints;
    private String identification; // 유저 식별용
}
