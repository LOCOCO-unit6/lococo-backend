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
    
//    // 콘텐츠 모아보기
//    @GetMapping("/content")
//    public ResponseEntity<List<ContentResponseDto>> getAllContents() {
//        List<ContentResponseDto> contents = myPageService.getAllContents();
//        return ResponseEntity.ok(contents);
//    }
    //관심 컨텐츠 출력하기
    @GetMapping("/{userId}/content/favorites")
    public ResponseEntity<List<ContentResponseDto>> getFavoriteContents(@PathVariable Long userId) {
        List<ContentResponseDto> favorites = myPageService.getFavoriteContents(userId);
        return ResponseEntity.ok(favorites);
    }


    // 현재 여정
    @GetMapping("/journeys/current")
    public ResponseEntity<JourneyResponseDto> getCurrentJourney() {
        JourneyResponseDto currentJourney = myPageService.getCurrentJourney();
        return ResponseEntity.ok(currentJourney);
    }

    // 개인 리뷰 조회
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getUserReviews(@PathVariable Long userId) {
        List<ReviewResponseDto> reviews = myPageService.getMyReviews(userId);
        return ResponseEntity.ok(reviews);
    }
    
    // 리뷰 작성
    @PostMapping("/{userId}/reviews")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequestDto requestDto) {
        Review createdReview = myPageService.createReview(requestDto);
        return ResponseEntity.ok(createdReview);
    }
    
    // 리뷰 수정
    @PutMapping("/{userId}/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto) {
        Review updatedReview = myPageService.updateReview(reviewId, requestDto);
        return ResponseEntity.ok(updatedReview);
    }




}
