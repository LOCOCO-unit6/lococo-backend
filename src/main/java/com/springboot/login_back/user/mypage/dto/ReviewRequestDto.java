package com.springboot.login_back.user.mypage.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewRequestDto {
    private Long userId;                    // 작성자 ID
    private String targetType;              // 여행지, 맛집 등
    private String targetName;              // 대상 이름
    private String location;                // 장소
    private String title;                   // 제목
    private Integer rating;                 // 평점
    private String content;                 // 후기글
    private List<String> imageUrls;         // 사진 첨부 (여러 개 가능)
    private String recommendation;          // 추천 한마디
}
