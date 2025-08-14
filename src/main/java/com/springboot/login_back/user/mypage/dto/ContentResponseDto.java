package com.springboot.login_back.mypage.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContentResponseDto {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String location;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
