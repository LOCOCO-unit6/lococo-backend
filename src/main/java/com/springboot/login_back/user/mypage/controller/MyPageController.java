package com.springboot.login_back.user.mypage.controller;


import com.springboot.login_back.model.User;
import com.springboot.login_back.user.mainpage.dto.ReviewResponseDto;
import com.springboot.login_back.user.mypage.model.Review;
import com.springboot.login_back.user.mypage.dto.ContentResponseDto;
import com.springboot.login_back.user.mypage.dto.JourneyResponseDto;
import com.springboot.login_back.user.mypage.dto.ReviewRequestDto;
import com.springboot.login_back.user.mypage.dto.UserUpdateRequestDto;
import com.springboot.login_back.user.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/mypage/{userId}")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 회원정보 수정
    @PutMapping("/users")
    public ResponseEntity<User> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto requestDto) {
        User updatedUser = myPageService.updateUserInfo(userId, requestDto);
        return ResponseEntity.ok(updatedUser);
    }

//    // 콘텐츠 모아보기
//    @GetMapping("/content")
//    public ResponseEntity<List<ContentResponseDto>> getAllContents() {
//        List<ContentResponseDto> contents = myPageService.getAllContents();
//        return ResponseEntity.ok(contents);
//    }
    //관심 컨텐츠 출력하기
    @GetMapping("/content/favorites")
    public ResponseEntity<List<ContentResponseDto>> getFavoriteContents(@PathVariable Long userId) {
        List<ContentResponseDto> favorites = myPageService.getFavoriteContents(userId);
        return ResponseEntity.ok(favorites);
    }


//    // 현재 여정
//    @GetMapping("/journeys/current")
//    public ResponseEntity<JourneyResponseDto> getCurrentJourney() {
//        JourneyResponseDto currentJourney = myPageService.getCurrentJourney();
//        return ResponseEntity.ok(currentJourney);
//    }

    // 이번 달 진행 중 여정 조회
    @GetMapping("/journeys/ongoing")
    public ResponseEntity<List<JourneyResponseDto>> getOngoingJourneys(@PathVariable Long userId) {
        List<JourneyResponseDto> journeys = myPageService.getOngoingJourneysThisMonth(userId);
        return ResponseEntity.ok(journeys);
    }

    // 이번 달 지난 여정 조회
    @GetMapping("/journeys/completed")
    public ResponseEntity<List<JourneyResponseDto>> getCompletedJourneys(@PathVariable Long userId) {
        List<JourneyResponseDto> journeys = myPageService.getCompletedJourneysThisMonth(userId);
        return ResponseEntity.ok(journeys);
    }

    // 여정 삭제
    @DeleteMapping("/journeys/{journeyId}")
    public ResponseEntity<Void> deleteJourney(@PathVariable Long userId, @PathVariable Long journeyId) {
        myPageService.deleteJourney(journeyId);
        return ResponseEntity.noContent().build();
    }

    // 여정 후기 작성
    @PostMapping("/journeys/{journeyId}/reviews")
    public ResponseEntity<Review> createReview(
            @PathVariable Long userId, // 이미 클래스 레벨 매핑으로 받음
            @PathVariable Long journeyId,
            @RequestBody ReviewRequestDto reviewRequestDto) {

        reviewRequestDto.setTargetType("JOURNEY");
        reviewRequestDto.setTargetName(myPageService.getJourneyTitle(journeyId));

        Review review = myPageService.createReviewForJourney(journeyId, reviewRequestDto);
        return ResponseEntity.ok(review);
    }



    // 개인 리뷰 조회
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getUserReviews(@PathVariable Long userId) {
        List<ReviewResponseDto> reviews = myPageService.getMyReviews(userId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequestDto requestDto) {
        Review createdReview = myPageService.createReview(requestDto);
        return ResponseEntity.ok(createdReview);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto) {
        Review updatedReview = myPageService.updateReview(reviewId, requestDto);
        return ResponseEntity.ok(updatedReview);
    }

}
