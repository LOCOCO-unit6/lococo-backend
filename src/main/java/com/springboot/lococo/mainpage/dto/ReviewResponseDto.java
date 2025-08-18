package com.springboot.lococo.mainpage.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {
    private Long id;
    private String title;
    private String content;
    private Integer rating;
    private String targetType;
    private String targetName;
    private String imageUrl;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
