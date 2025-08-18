package com.springboot.lococo.mainpage.service;

import com.springboot.lococo.mainpage.dto.*;
import com.springboot.lococo.mainpage.model.*;
import com.springboot.lococo.mainpage.repository.*;
import com.springboot.lococo.mainpage.model.ReviewList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MainPageService {
    
    private final RecommendedCourseRepository recommendedCourseRepository;
    private final MainPageReviewRepository reviewRepository;
    
    // 추천 코스 조회
    @Transactional(readOnly = true)
    public List<RecommendedCourseResponseDto> getRecommendedCourses() {
        List<RecommendedCourse> courses = recommendedCourseRepository.findTop10ByOrderByViewCountDesc();
        return courses.stream()
                .map(this::convertToRecommendedCourseDto)
                .collect(Collectors.toList());
    }
    
    // 카테고리별 추천 코스 조회
    @Transactional(readOnly = true)
    public List<RecommendedCourseResponseDto> getRecommendedCoursesByCategory(String category) {
        List<RecommendedCourse> courses = recommendedCourseRepository.findByCategoryOrderByLikeCountDesc(category);
        return courses.stream()
                .map(this::convertToRecommendedCourseDto)
                .collect(Collectors.toList());
    }
    
    // 이용 후기 조회 (최신순)
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getRecentReviews() {
        List<ReviewList> reviewLists = reviewRepository.findTop10ByOrderByCreatedAtDesc();
        return reviewLists.stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());
    }
    
    // 평점별 이용 후기 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByRating(Integer minRating) {
        List<ReviewList> reviewLists = reviewRepository.findByRatingGreaterThanEqualOrderByCreatedAtDesc(minRating);
        return reviewLists.stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());
    }
    
    private RecommendedCourseResponseDto convertToRecommendedCourseDto(RecommendedCourse course) {
        RecommendedCourseResponseDto dto = new RecommendedCourseResponseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDestination(course.getDestination());
        dto.setCategory(course.getCategory());
        dto.setDuration(course.getDuration());
        dto.setDifficulty(course.getDifficulty());
        dto.setImageUrl(course.getImageUrl());
        dto.setMapUrl(course.getMapUrl());
        dto.setViewCount(course.getViewCount());
        dto.setLikeCount(course.getLikeCount());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        return dto;
    }
    
    private ReviewResponseDto convertToReviewDto(ReviewList reviewList) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(reviewList.getId());
        dto.setTitle(reviewList.getTitle());
        dto.setContent(reviewList.getContent());
        dto.setRating(reviewList.getRating());
        dto.setTargetType(reviewList.getTargetType());
        dto.setTargetName(reviewList.getTargetName());
        dto.setImageUrl(reviewList.getImageUrl());
        dto.setUserId(reviewList.getUserId());
        dto.setCreatedAt(reviewList.getCreatedAt());
        dto.setUpdatedAt(reviewList.getUpdatedAt());
        return dto;
    }
}
