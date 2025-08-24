package com.springboot.lococo.mypage.controller;

import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.*;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 회원정보 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto requestDto) {
        User updatedUser = myPageService.updateUserInfo(userId, requestDto);
        return ResponseEntity.ok(updatedUser);
    }

    // 콘텐츠 모아보기
    @GetMapping("/content")
    public ResponseEntity<List<ContentResponseDto>> getAllContents() {
        List<ContentResponseDto> contents = myPageService.getAllContents();
        return ResponseEntity.ok(contents);
    }

    // 관심 콘텐츠 모아보기
    @GetMapping("/content/favorites")
    public ResponseEntity<List<ContentResponseDto>> getFavoriteContents() {
        List<ContentResponseDto> favoriteContents = myPageService.getFavoriteContents();
        return ResponseEntity.ok(favoriteContents);
    }

    // 이번 달 진행 중 일정
    @GetMapping("/schedules/ongoing")
    public ResponseEntity<List<TravelScheduleResponseDto>> getOngoingSchedules() {
        List<TravelScheduleResponseDto> schedules = myPageService.getOngoingSchedules();
        return ResponseEntity.ok(schedules);
    }

    // 지난 일정
    @GetMapping("/schedules/completed")
    public ResponseEntity<List<TravelScheduleResponseDto>> getCompletedSchedules() {
        List<TravelScheduleResponseDto> schedules = myPageService.getCompletedSchedules();
        return ResponseEntity.ok(schedules);
    }


    // 여정 삭제
    @DeleteMapping("/journeys/{journeyId}")
    public ResponseEntity<Void> deleteJourney(@PathVariable Long journeyId) {
        myPageService.deleteJourney(journeyId);
        return ResponseEntity.noContent().build();
    }

    // 여정 리뷰 작성
    @PostMapping("/journeys/{journeyId}/reviews")
    public ResponseEntity<UserReview> createJourneyReview(
            @PathVariable Long journeyId,
            @RequestBody ReviewRequestDto requestDto,
            @RequestParam Long userId // 로그인 사용자 ID 전달
    ) {
        UserReview review = myPageService.createJourneyReview(journeyId, requestDto, userId);
        return ResponseEntity.ok(review);
    }

    // 일반 리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<UserReview> createReview(
            @RequestBody ReviewRequestDto requestDto,
            @RequestParam Long userId // 로그인 사용자 ID 전달
    ) {
        UserReview review = myPageService.createReview(requestDto, userId);
        return ResponseEntity.ok(review);
    }

    // 리뷰 조회
    @GetMapping("/reviews")
    public ResponseEntity<List<SimpleReviewResponseDto>> getReviews() {
        List<SimpleReviewResponseDto> reviews = myPageService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<UserReview> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto
    ) {
        UserReview updated = myPageService.updateReview(reviewId, requestDto);
        return ResponseEntity.ok(updated);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        myPageService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
