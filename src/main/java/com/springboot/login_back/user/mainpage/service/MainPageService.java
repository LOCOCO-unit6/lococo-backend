package com.springboot.login_back.user.mainpage.service;

import com.springboot.login_back.user.mainpage.dto.RecommendedCourseResponseDto;
import com.springboot.login_back.user.mainpage.dto.ReviewResponseDto;
import com.springboot.login_back.user.mainpage.repository.RecommendedCourseRepository;
import com.springboot.login_back.user.mypage.repository.ReviewRepository;
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
    private final ReviewRepository reviewRepository;

    // 추천 코스 조회 (상위 10개, 조회수 기준)
    @Transactional(readOnly = true)
    public List<RecommendedCourseResponseDto> getRecommendedCourses() {
        return recommendedCourseRepository.findTop10ByOrderByViewCountDesc()
                .stream()
                .map(course -> {
                    RecommendedCourseResponseDto dto = new RecommendedCourseResponseDto();
                    dto.setTitle(course.getTitle());
                    dto.setLocation(course.getLocation());
                    dto.setImageUrl(course.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 카테고리별 추천 코스 조회 (좋아요 수 기준)
    @Transactional(readOnly = true)
    public List<RecommendedCourseResponseDto> getRecommendedCoursesByCategory(String category) {
        return recommendedCourseRepository.findByCategoryOrderByLikeCountDesc(category)
                .stream()
                .map(course -> {
                    RecommendedCourseResponseDto dto = new RecommendedCourseResponseDto();
                    dto.setTitle(course.getTitle());
                    dto.setLocation(course.getLocation());
                    dto.setImageUrl(course.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 이용 후기 조회 (최신순 상위 10개)
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getRecentReviews() {
        return reviewRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(review -> {
                    ReviewResponseDto dto = new ReviewResponseDto();
                    dto.setTitle(review.getTitle());
                    dto.setContent(review.getContent());
                    dto.setRating(review.getRating());
                    dto.setImageUrls(review.getImageUrls());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 평점별 이용 후기 조회 (minRating 이상)
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByRating(Integer minRating) {
        return reviewRepository.findByRatingGreaterThanEqualOrderByCreatedAtDesc(minRating)
                .stream()
                .map(review -> {
                    ReviewResponseDto dto = new ReviewResponseDto();
                    dto.setTitle(review.getTitle());
                    dto.setContent(review.getContent());
                    dto.setRating(review.getRating());
                    dto.setImageUrls(review.getImageUrls());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
