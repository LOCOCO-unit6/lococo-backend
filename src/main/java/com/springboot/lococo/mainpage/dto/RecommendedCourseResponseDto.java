package com.springboot.lococo.user.mainpage.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecommendedCourseResponseDto {
    private Long id;
    private String title;
    private String description;
    private String destination;
    private String category;
    private Integer duration;
    private Integer difficulty;
    private String imageUrl;
    private String mapUrl;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
