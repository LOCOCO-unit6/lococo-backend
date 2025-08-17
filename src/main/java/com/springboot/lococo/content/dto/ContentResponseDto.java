package com.springboot.lococo.content.dto;

import com.springboot.lococo.content.model.ContentEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ContentResponseDto {
    private final Long id; // ✅ ID 필드가 존재합니다.
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String organizer;
    private String title;
    private String text;
    private String imageUrl;


    public ContentResponseDto(ContentEntity entity) {
        this.id = entity.getId();

        this.name = entity.getName();
        this.location = entity.getLocation();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.organizer = entity.getOrganizer();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.imageUrl = entity.getImageUrl();
    }
}
