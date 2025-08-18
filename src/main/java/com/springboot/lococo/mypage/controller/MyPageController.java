package com.springboot.lococo.mypage.controller;


import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.ContentResponseDto;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.service.MyPageService;
import com.springboot.lococo.mypage.dto.JourneyResponseDto;
import com.springboot.lococo.mypage.dto.ReviewRequestDto;
import com.springboot.lococo.mypage.dto.UserUpdateRequestDto;
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
    public ResponseEntity<UserReview> createReview(@RequestBody ReviewRequestDto requestDto) {
        UserReview createdUserReview = myPageService.createReview(requestDto);
        return ResponseEntity.ok(createdUserReview);
    }
    
    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<UserReview> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto) {
        UserReview updatedUserReview = myPageService.updateReview(reviewId, requestDto);
        return ResponseEntity.ok(updatedUserReview);
    }
}
