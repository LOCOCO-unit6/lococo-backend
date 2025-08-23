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

    // 이번 달 진행 중 여정
    @GetMapping("/journeys/ongoing")
    public ResponseEntity<List<JourneyResponseDto>> getOngoingJourneys() {
        List<JourneyResponseDto> journeys = myPageService.getOngoingJourneys();
        return ResponseEntity.ok(journeys);
    }

    // 이번 달 지난 여정
    @GetMapping("/journeys/completed")
    public ResponseEntity<List<JourneyResponseDto>> getCompletedJourneys() {
        List<JourneyResponseDto> journeys = myPageService.getCompletedJourneys();
        return ResponseEntity.ok(journeys);
    }

    // 여정 삭제
    @DeleteMapping("/journeys/{journeyId}")
    public ResponseEntity<Void> deleteJourney(@PathVariable Long journeyId) {
        myPageService.deleteJourney(journeyId);
        return ResponseEntity.noContent().build();
    }

    // 여정 후기 작성
    @PostMapping("/journeys/{journeyId}/reviews")
    public ResponseEntity<UserReview> createJourneyReview(
            @PathVariable Long journeyId,
            @RequestBody ReviewRequestDto requestDto) {
        UserReview review = myPageService.createJourneyReview(journeyId, requestDto);
        return ResponseEntity.ok(review);
    }

    // 리뷰 조회
// 리뷰 조회
    @GetMapping("/reviews")
    public ResponseEntity<List<SimpleReviewResponseDto>> getReviews() {
        List<SimpleReviewResponseDto> reviews = myPageService.getReviews();
        return ResponseEntity.ok(reviews);
    }


    // 리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<UserReview> createReview(@RequestBody ReviewRequestDto requestDto) {
        return ResponseEntity.ok(myPageService.createReview(requestDto));
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<UserReview> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto) {
        return ResponseEntity.ok(myPageService.updateReview(reviewId, requestDto));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        myPageService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
