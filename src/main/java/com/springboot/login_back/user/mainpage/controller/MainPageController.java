package com.springboot.login_back.user.mainpage.controller;

import com.springboot.login_back.user.mainpage.dto.*;
import com.springboot.login_back.user.mainpage.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainPageController {
    
    private final MainPageService mainPageService;
    
    // 추천 코스 조회
    @GetMapping("/recommended-courses")
    public ResponseEntity<List<RecommendedCourseResponseDto>> getRecommendedCourses() {
        List<RecommendedCourseResponseDto> courses = mainPageService.getRecommendedCourses();
        return ResponseEntity.ok(courses);
    }
    
    // 카테고리별 추천 코스 조회
    @GetMapping("/recommended-courses/category/{category}")
    public ResponseEntity<List<RecommendedCourseResponseDto>> getRecommendedCoursesByCategory(
            @PathVariable String category) {
        List<RecommendedCourseResponseDto> courses = mainPageService.getRecommendedCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }
    
    // 이용 후기 조회 (최신순)
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getRecentReviews() {
        List<ReviewResponseDto> reviews = mainPageService.getRecentReviews();
        return ResponseEntity.ok(reviews);
    }
    
    // 평점별 이용 후기 조회
    @GetMapping("/reviews/rating/{minRating}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByRating(
            @PathVariable Integer minRating) {
        List<ReviewResponseDto> reviews = mainPageService.getReviewsByRating(minRating);
        return ResponseEntity.ok(reviews);
    }
}
