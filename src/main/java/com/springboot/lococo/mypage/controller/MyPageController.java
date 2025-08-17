package com.springboot.lococo.user.mypage.controller;


import com.springboot.lococo.model.User;
import com.springboot.lococo.user.mypage.model.Review;
import com.springboot.lococo.user.mypage.dto.ContentResponseDto;
import com.springboot.lococo.user.mypage.dto.JourneyResponseDto;
import com.springboot.lococo.user.mypage.dto.ReviewRequestDto;
import com.springboot.lococo.user.mypage.dto.UserUpdateRequestDto;
import com.springboot.lococo.user.mypage.service.MyPageService;
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
    
    // 현재 여정
    @GetMapping("/journeys/current")
    public ResponseEntity<JourneyResponseDto> getCurrentJourney() {
        JourneyResponseDto currentJourney = myPageService.getCurrentJourney();
        return ResponseEntity.ok(currentJourney);
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
