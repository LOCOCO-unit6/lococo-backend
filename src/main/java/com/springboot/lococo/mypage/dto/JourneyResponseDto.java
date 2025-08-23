package com.springboot.lococo.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.lococo.mypage.model.Journey;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JourneyResponseDto {

    private Long id;
    private String title;
    private String description;
    private String destination;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public JourneyResponseDto(Journey journey) {
        this.id = journey.getId();
        this.title = journey.getTitle();
        this.description = journey.getDescription();
        this.destination = journey.getDestination();
        this.startDate = journey.getStartDate();
        this.endDate = journey.getEndDate();
        this.status = journey.getStatus().name();
        this.createdAt = journey.getCreatedAt();
        this.updatedAt = journey.getUpdatedAt();
    }
}
