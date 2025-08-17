package com.springboot.lococo.organizer.main.dto;



import lombok.Data;

@Data
public class FeatureCardDto {
    private Long id;
    private String icon;
    private String title;
    private String description;
    private Integer displayOrder;
}