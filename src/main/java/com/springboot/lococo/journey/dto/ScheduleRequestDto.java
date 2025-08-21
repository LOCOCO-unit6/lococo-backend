package com.springboot.lococo.journey.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ScheduleRequestDto {
    private String startDate;
    private String endDate;
    private String location;
    private List<String> keywords;
    private List<String> ageGroups;
    private String companion;
    private List<String> timeOfDay;
}
