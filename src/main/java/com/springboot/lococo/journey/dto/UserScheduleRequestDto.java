package com.springboot.lococo.journey.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserScheduleRequestDto {
    private String title;          // 일정 제목
    private String summary;        // 일정 요약
    private LocalDate date;        // 일정 날짜
    private String location;       // 지역
    private List<ActivityDto> activities; // 활동 목록
}
