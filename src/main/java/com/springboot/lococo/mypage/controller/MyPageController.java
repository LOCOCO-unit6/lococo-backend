package com.springboot.lococo.mypage.controller;

import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.*;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal User user) {

        //로그인 안하고 접근할 경우
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        UserInfoResponseDto responseDto = UserInfoResponseDto.from(user);
        return ResponseEntity.ok(responseDto);
    }

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

    // 찜하기 추가
    @PostMapping("/content/{contentId}/favorite")
    public ResponseEntity<com.springboot.lococo.content.dto.ContentResponseDto> addFavoriteContent(
            @AuthenticationPrincipal User user,
            @PathVariable Long contentId) {
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        myPageService.addFavoriteContent(user, contentId);
        com.springboot.lococo.content.dto.ContentResponseDto responseDto = 
            new com.springboot.lococo.content.dto.ContentResponseDto(
                myPageService.getContentById(contentId)
            );
        return ResponseEntity.ok(responseDto);
    }

    // 찜하기 삭제
    @DeleteMapping("/content/{contentId}/favorite")
    public ResponseEntity<Void> removeFavoriteContent(
            @AuthenticationPrincipal User user,
            @PathVariable Long contentId) {
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        myPageService.removeFavoriteContent(user, contentId);
        return ResponseEntity.noContent().build();
    }

    // 유저별 찜한 콘텐츠 목록 조회 (마이페이지 카드 기준)
    @GetMapping("/content/favorites")
    public ResponseEntity<List<com.springboot.lococo.mypage.dto.ContentResponseDto>> getUserFavoriteContents(
            @AuthenticationPrincipal User user) {
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<com.springboot.lococo.mypage.dto.ContentResponseDto> favoriteContents = 
            myPageService.getUserFavoriteMypageContents(user);
        return ResponseEntity.ok(favoriteContents);
    }

    // 찜하기 토글 (추가/삭제)
    @PostMapping("/content/{contentId}/favorite/toggle")
    public ResponseEntity<com.springboot.lococo.content.dto.ContentResponseDto> toggleFavoriteContent(
            @AuthenticationPrincipal User user,
            @PathVariable Long contentId) {
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        boolean isFavorite = myPageService.isFavoriteContent(user, contentId);
        if (isFavorite) {
            myPageService.removeFavoriteContent(user, contentId);
        } else {
            myPageService.addFavoriteContent(user, contentId);
        }
        
        com.springboot.lococo.content.dto.ContentResponseDto responseDto = 
            new com.springboot.lococo.content.dto.ContentResponseDto(
                myPageService.getContentById(contentId)
            );
        return ResponseEntity.ok(responseDto);
    }
}
