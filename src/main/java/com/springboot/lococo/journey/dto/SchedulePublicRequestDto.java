package com.springboot.lococo.journey.dto;

import lombok.Data;
import java.util.List;

@Data
public class SchedulePublicRequestDto {
    private String startDate;
    private String endDate;
    private String location;
    private List<String> keywords;
    private List<String> ageGroups;
    private String companion;
    private List<String> timeOfDay;
}
