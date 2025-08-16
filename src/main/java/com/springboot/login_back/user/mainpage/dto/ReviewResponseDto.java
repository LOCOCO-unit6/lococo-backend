package com.springboot.login_back.user.mainpage.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewResponseDto {
    private LocalDateTime createdAt;        // 작성 날짜
    private String location;                // 장소
    private String title;                   // 제목
    private Integer rating;                 // 평점
    private String content;                 // 후기글
    private List<String> imageUrls;         // 사진 첨부 (여러 개 가능)
    private String recommendation;          // 추천 한마디
}
