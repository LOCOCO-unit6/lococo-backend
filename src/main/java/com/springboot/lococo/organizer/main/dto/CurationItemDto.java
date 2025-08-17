package com.springboot.lococo.organizer.main.dto;


import lombok.Data;

@Data
public class CurationItemDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Integer displayOrder;
}