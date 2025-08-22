package com.springboot.lococo.userai.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SurveyRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private List<String> keywords;
    private String ageRange;
    private List<String> companions;
    private String travelStyle;
    private Integer dailyBudget;
    private List<String> mustVisit;
    private String note;
    private List<String> imageHints;
}
