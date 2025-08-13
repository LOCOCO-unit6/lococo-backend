package com.springboot.login_back.mypage.dto;

import lombok.Data;

@Data
public class ReviewRequestDto {
    private String title;
    private String content;
    private Integer rating;
    private String targetType;
    private String targetName;
    private String imageUrl;
}
