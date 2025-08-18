package com.springboot.lococo.mypage.service;


import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.model.UserMypageContent;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.repository.UserContentRepository;
import com.springboot.lococo.repository.UserRepository;
import com.springboot.lococo.mypage.dto.ContentResponseDto;
import com.springboot.lococo.mypage.dto.JourneyResponseDto;
import com.springboot.lococo.mypage.dto.ReviewRequestDto;
import com.springboot.lococo.mypage.dto.UserUpdateRequestDto;
import com.springboot.lococo.mypage.model.Journey;
import com.springboot.lococo.mypage.repository.JourneyRepository;
import com.springboot.lococo.mypage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    
    private final UserRepository userRepository;
    private final UserContentRepository userContentRepository;
    private final JourneyRepository journeyRepository;
    private final ReviewRepository reviewRepository;
    
    // 회원정보 수정
    public User updateUserInfo(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        if (requestDto.getEmail() != null) {
            user.setEmail(requestDto.getEmail());
        }
        if (requestDto.getPhoneNumber() != null) {
            user.setPhoneNumber(requestDto.getPhoneNumber());
        }
        if (requestDto.getAffiliation() != null) {
            user.setAffiliation(requestDto.getAffiliation());
        }
        
        return userRepository.save(user);
    }
    
    // 콘텐츠 모아보기
    @Transactional(readOnly = true)
    public List<ContentResponseDto> getAllContents() {
        List<UserMypageContent> contents = userContentRepository.findAll();
        return contents.stream()
                .map(this::convertToContentDto)
                .collect(Collectors.toList());
    }
    
    // 현재 여정 조회
    @Transactional(readOnly = true)
    public JourneyResponseDto getCurrentJourney() {
        Journey currentJourney = journeyRepository
                .findFirstByStatusOrderByStartDateDesc(Journey.JourneyStatus.ONGOING)
                .orElseThrow(() -> new RuntimeException("진행 중인 여정이 없습니다."));
        
        return convertToJourneyDto(currentJourney);
    }
    
    // 리뷰 작성
    public UserReview createReview(ReviewRequestDto requestDto) {
        UserReview userReview = new UserReview();
        userReview.setTitle(requestDto.getTitle());
        userReview.setContent(requestDto.getContent());
        userReview.setRating(requestDto.getRating());
        userReview.setTargetType(requestDto.getTargetType());
        userReview.setTargetName(requestDto.getTargetName());
        userReview.setImageUrl(requestDto.getImageUrl());
        userReview.setUserId(requestDto.getUserId());
        
        return reviewRepository.save(userReview);
    }
    
    // 리뷰 수정
    public UserReview updateReview(Long reviewId, ReviewRequestDto requestDto) {
        UserReview userReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        
        if (requestDto.getTitle() != null) {
            userReview.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            userReview.setContent(requestDto.getContent());
        }
        if (requestDto.getRating() != null) {
            userReview.setRating(requestDto.getRating());
        }
        if (requestDto.getTargetType() != null) {
            userReview.setTargetType(requestDto.getTargetType());
        }
        if (requestDto.getTargetName() != null) {
            userReview.setTargetName(requestDto.getTargetName());
        }
        if (requestDto.getImageUrl() != null) {
            userReview.setImageUrl(requestDto.getImageUrl());
        }
        
        return reviewRepository.save(userReview);
    }
    
    private ContentResponseDto convertToContentDto(UserMypageContent content) {
        ContentResponseDto dto = new ContentResponseDto();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setDescription(content.getDescription());
        dto.setType(content.getType());
        dto.setLocation(content.getLocation());
        dto.setImageUrl(content.getImageUrl());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        return dto;
    }
    
    private JourneyResponseDto convertToJourneyDto(Journey journey) {
        JourneyResponseDto dto = new JourneyResponseDto();
        dto.setId(journey.getId());
        dto.setTitle(journey.getTitle());
        dto.setDescription(journey.getDescription());
        dto.setDestination(journey.getDestination());
        dto.setStartDate(journey.getStartDate());
        dto.setEndDate(journey.getEndDate());
        dto.setStatus(journey.getStatus().name());
        dto.setCreatedAt(journey.getCreatedAt());
        dto.setUpdatedAt(journey.getUpdatedAt());
        return dto;
    }
}
