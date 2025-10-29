package com.springboot.lococo.mypage.controller;

import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.*;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.service.MyPageService;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
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

    // 진행 중인 일정 조회
    @GetMapping("/schedules/ongoing")
    public ResponseEntity<List<ScheduleResponseDto>> getOngoingSchedules() {
        List<ScheduleResponseDto> schedules = myPageService.getOngoingSchedules();
        return ResponseEntity.ok(schedules);
    }

    // 완료된 일정 조회
    @GetMapping("/schedules/completed")
    public ResponseEntity<List<ScheduleResponseDto>> getCompletedSchedules() {
        List<ScheduleResponseDto> schedules = myPageService.getCompletedSchedules();
        return ResponseEntity.ok(schedules);
    }

    // 사용자별 진행 중인 일정 조회
    @GetMapping("/schedules/ongoing/user/{userId}")
    public ResponseEntity<List<ScheduleResponseDto>> getOngoingSchedulesByUser(@PathVariable Long userId) {
        List<ScheduleResponseDto> schedules = myPageService.getOngoingSchedulesByUser(userId);
        return ResponseEntity.ok(schedules);
    }

    // 사용자별 완료된 일정 조회
    @GetMapping("/schedules/completed/user/{userId}")
    public ResponseEntity<List<ScheduleResponseDto>> getCompletedSchedulesByUser(@PathVariable Long userId) {
        List<ScheduleResponseDto> schedules = myPageService.getCompletedSchedulesByUser(userId);
        return ResponseEntity.ok(schedules);
    }

    // 현재 일정 조회
    @GetMapping("/schedules/current")
    public ResponseEntity<ScheduleResponseDto> getCurrentSchedule() {
        ScheduleResponseDto currentSchedule = myPageService.getCurrentSchedule();
        return ResponseEntity.ok(currentSchedule);
    }

    // 일정 삭제
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        myPageService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    // 통합 일정 후기 작성 (AI 생성 + 사용자 생성 일정 모두 가능)
    @PostMapping("/schedules/{scheduleId}/reviews")
    public ResponseEntity<UserReview> createScheduleReview(
            @PathVariable Long scheduleId,
            @RequestBody ReviewRequestDto requestDto) {
        UserReview review = myPageService.createJourneyReview(scheduleId, requestDto);
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
