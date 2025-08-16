package com.springboot.login_back.user.mypage.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JourneyResponseDto {
    private Long id;
    private String title;
    private String description;
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
