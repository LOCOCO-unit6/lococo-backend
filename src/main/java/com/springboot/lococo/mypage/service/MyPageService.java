package com.springboot.lococo.mypage.service;

import com.springboot.lococo.journey.repository.TravelScheduleRepository;
import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.*;
import com.springboot.lococo.mypage.model.Journey;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.repository.JourneyRepository;
import com.springboot.lococo.mypage.repository.ReviewRepository;
import com.springboot.lococo.mypage.repository.UserContentRepository;
import com.springboot.lococo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final UserContentRepository userContentRepository;
    private final JourneyRepository journeyRepository;
    private final ReviewRepository reviewRepository;
    private final TravelScheduleRepository travelScheduleRepository;

    // 회원정보 수정
    public User updateUserInfo(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        return userRepository.save(user);
    }

    // 콘텐츠 조회
    public List<ContentResponseDto> getAllContents() {
        return userContentRepository.findAll()
                .stream()
                .map(ContentResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ContentResponseDto> getFavoriteContents() {
        return userContentRepository.findFavorites()
                .stream()
                .map(ContentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 진행 중 일정 조회
    public List<TravelScheduleResponseDto> getOngoingSchedules() {
        LocalDate today = LocalDate.now();
        return travelScheduleRepository.findByDateGreaterThanEqualOrderByDateAsc(today)
                .stream()
                .map(TravelScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    // 지난 일정 조회
    public List<TravelScheduleResponseDto> getCompletedSchedules() {
        LocalDate today = LocalDate.now();
        return travelScheduleRepository.findByDateBeforeOrderByDateDesc(today)
                .stream()
                .map(TravelScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

//    // 현재 여정 (단일)
//    public JourneyResponseDto getCurrentJourney() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Journey> ongoing = journeyRepository.findByStartDateBeforeAndEndDateAfterOrderByStartDateAsc(now, now);
//        return ongoing.isEmpty() ? null : new JourneyResponseDto(ongoing.get(0));
//    }

    // 여정 삭제
    public void deleteJourney(Long journeyId) {
        journeyRepository.deleteById(journeyId);
    }

    // 여정 후기 작성
    public UserReview createJourneyReview(Long journeyId, ReviewRequestDto requestDto, Long userId) {
        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserReview review = new UserReview();
        review.setJourney(journey);
        review.setUser(user); // 필수
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setTitle(requestDto.getTitle());
        review.setLocation(requestDto.getLocation());
        review.setRecommendation(requestDto.getRecommendation());
        review.setImageUrls(requestDto.getImageUrls());
        review.setCreatedAt(LocalDateTime.now()); // 서버에서 자동 처리

        return reviewRepository.save(review);
    }

    // 일반 리뷰 작성
    public UserReview createReview(ReviewRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserReview review = new UserReview();
        review.setUser(user);
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setTitle(requestDto.getTitle());
        review.setLocation(requestDto.getLocation());
        review.setRecommendation(requestDto.getRecommendation());
        review.setImageUrls(requestDto.getImageUrls());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // 리뷰 조회 (Simple DTO)
    public List<SimpleReviewResponseDto> getReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(SimpleReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // 리뷰 수정
    public UserReview updateReview(Long reviewId, ReviewRequestDto requestDto) {
        UserReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setTitle(requestDto.getTitle());
        review.setLocation(requestDto.getLocation());
        review.setRecommendation(requestDto.getRecommendation());
        review.setImageUrls(requestDto.getImageUrls());

        return reviewRepository.save(review);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
