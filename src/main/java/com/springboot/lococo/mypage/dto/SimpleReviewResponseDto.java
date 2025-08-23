package com.springboot.lococo.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.lococo.mypage.model.UserReview;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SimpleReviewResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private String location;
    private String title;
    private int rating;
    private String content;
    private List<String> imageUrls;
    private String recommendation;

    public SimpleReviewResponseDto(UserReview review) {
        this.createdAt = review.getCreatedAt();
        this.location = review.getLocation();
        this.title = review.getTitle();
        this.rating = review.getRating();
        this.content = review.getComment();
        this.imageUrls = review.getImageUrls();
        this.recommendation = review.getRecommendation();
    }
}
