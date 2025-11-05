package com.springboot.lococo.mypage.service;

import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.content.repository.ContentRepository;
import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.*;
import com.springboot.lococo.mypage.model.FavoriteContent;
import com.springboot.lococo.mypage.model.Journey;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.model.UserMypageContent;
import com.springboot.lococo.mypage.repository.FavoriteContentRepository;
import com.springboot.lococo.mypage.repository.JourneyRepository;
import com.springboot.lococo.mypage.repository.ReviewRepository;
import com.springboot.lococo.mypage.repository.UserContentRepository;
import com.springboot.lococo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ContentRepository contentRepository;
    private final FavoriteContentRepository favoriteContentRepository;

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

    // 진행 중 여정
    public List<JourneyResponseDto> getOngoingJourneys() {
        LocalDateTime now = LocalDateTime.now();
        return journeyRepository.findByStartDateBeforeAndEndDateAfterOrderByStartDateAsc(now, now)
                .stream()
                .map(JourneyResponseDto::new)
                .collect(Collectors.toList());
    }

    // 지난 여정
    public List<JourneyResponseDto> getCompletedJourneys() {
        LocalDateTime now = LocalDateTime.now();
        return journeyRepository.findByEndDateBeforeOrderByEndDateDesc(now)
                .stream()
                .map(JourneyResponseDto::new)
                .collect(Collectors.toList());
    }

    // 현재 여정 (단일)
    public JourneyResponseDto getCurrentJourney() {
        LocalDateTime now = LocalDateTime.now();
        List<Journey> ongoing = journeyRepository.findByStartDateBeforeAndEndDateAfterOrderByStartDateAsc(now, now);
        return ongoing.isEmpty() ? null : new JourneyResponseDto(ongoing.get(0));
    }

    // 여정 삭제
    public void deleteJourney(Long journeyId) {
        journeyRepository.deleteById(journeyId);
    }

    // 여정 후기 작성
    public UserReview createJourneyReview(Long journeyId, ReviewRequestDto requestDto) {
        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(() -> new IllegalArgumentException("Journey not found"));
        UserReview review = new UserReview();
        review.setJourney(journey);
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        return reviewRepository.save(review);
    }

    // 리뷰 조회 (Simple DTO)
    public List<SimpleReviewResponseDto> getReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(SimpleReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // 리뷰 작성
    public UserReview createReview(ReviewRequestDto requestDto) {
        UserReview review = new UserReview();
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        return reviewRepository.save(review);
    }

    // 리뷰 수정
    public UserReview updateReview(Long reviewId, ReviewRequestDto requestDto) {
        UserReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        return reviewRepository.save(review);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 찜하기 추가
    @Transactional
    public FavoriteContent addFavoriteContent(User user, Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));

        // 이미 찜한 콘텐츠인지 확인
        if (favoriteContentRepository.existsByUserAndContent(user, content)) {
            throw new IllegalArgumentException("이미 찜한 콘텐츠입니다.");
        }

        FavoriteContent favoriteContent = new FavoriteContent(user, content);
        FavoriteContent saved = favoriteContentRepository.save(favoriteContent);

        // 마이페이지 카드 동기화 (upsert)
        UserMypageContent my = userContentRepository.findByUserAndContentId(user, contentId)
                .orElseGet(UserMypageContent::new);
        my.setUser(user);
        my.setContentId(contentId);
        my.setTitle(content.getTitle());
        my.setRegion(content.getLocation());
        my.setDescription(content.getText());
        my.setImageUrl(content.getImageUrl());
        my.setFavorite(true);
        userContentRepository.save(my);

        return saved;
    }

    // 찜하기 삭제
    @Transactional
    public void removeFavoriteContent(User user, Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));

        FavoriteContent favoriteContent = favoriteContentRepository.findByUserAndContent(user, content)
                .orElseThrow(() -> new IllegalArgumentException("찜한 콘텐츠가 아닙니다."));

        favoriteContentRepository.delete(favoriteContent);

        // 마이페이지 카드 동기화 (favorite false)
        userContentRepository.findByUserAndContentId(user, contentId)
                .ifPresent(my -> {
                    my.setFavorite(false);
                    userContentRepository.save(my);
                });
    }

    // 유저별 찜한 콘텐츠 목록 조회 (마이페이지 카드 기준)
    public List<com.springboot.lococo.mypage.dto.ContentResponseDto> getUserFavoriteMypageContents(User user) {
        return userContentRepository.findByUserAndFavoriteTrueOrderByIdDesc(user)
                .stream()
                .map(com.springboot.lococo.mypage.dto.ContentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 찜하기 여부 확인
    public boolean isFavoriteContent(User user, Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));
        return favoriteContentRepository.existsByUserAndContent(user, content);
    }

    // 콘텐츠 ID로 조회
    public ContentEntity getContentById(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));
    }
}
