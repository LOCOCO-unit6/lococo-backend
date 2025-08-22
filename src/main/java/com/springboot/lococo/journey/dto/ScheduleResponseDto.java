package com.springboot.lococo.journey.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponseDto {
    private String date;              // yyyy-MM-dd
    private String location;          // 지역
    private String title;             // 여정 이름
    private String summary;           // 세부정보 (짧게)

    private List<ActivityDto> activities;
}

