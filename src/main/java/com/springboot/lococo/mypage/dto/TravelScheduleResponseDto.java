package com.springboot.lococo.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.lococo.journey.model.TravelSchedule;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TravelScheduleResponseDto {

    private Long id;
    private String title;
    private String summary;
    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    public TravelScheduleResponseDto(TravelSchedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.summary = schedule.getSummary();
        this.location = schedule.getLocation();
        this.date = schedule.getDate();
    }
}
