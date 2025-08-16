package com.springboot.login_back.user.mainpage.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendedCourseResponseDto {
    private String title;
    private String imageUrl;
    private String location;


}
