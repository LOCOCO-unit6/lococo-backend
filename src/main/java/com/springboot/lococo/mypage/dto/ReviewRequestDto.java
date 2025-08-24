package com.springboot.lococo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequestDto {

    private int rating;                  // 평점
    private String comment;              // 리뷰 내용
    private String title;                // 리뷰 제목
    private String location;             // 장소
    private List<String> imageUrls;      // 이미지 리스트
    private String recommendation;       // 추천 한마디
    private String journeyTitle;         // 여정 제목 (선택)
}
