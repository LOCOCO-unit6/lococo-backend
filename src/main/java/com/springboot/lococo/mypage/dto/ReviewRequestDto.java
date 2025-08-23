package com.springboot.lococo.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private String journeyTitle;         // 여정 제목 (추가)

    // 작성일자 (프론트에서 보내거나 서버에서 now() 처리 가능)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
