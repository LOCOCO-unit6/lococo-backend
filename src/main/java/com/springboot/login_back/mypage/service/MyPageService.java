package com.springboot.login_back.mypage.service;

import com.springboot.login_back.mypage.dto.*;
import com.springboot.login_back.mypage.model.*;
import com.springboot.login_back.mypage.repository.*;
import com.springboot.login_back.model.User;
import com.springboot.login_back.repository.UserRepository;
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
    private final ContentRepository contentRepository;
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
        List<Content> contents = contentRepository.findAll();
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
    public Review createReview(ReviewRequestDto requestDto) {
        Review review = new Review();
        review.setTitle(requestDto.getTitle());
        review.setContent(requestDto.getContent());
        review.setRating(requestDto.getRating());
        review.setTargetType(requestDto.getTargetType());
        review.setTargetName(requestDto.getTargetName());
        review.setImageUrl(requestDto.getImageUrl());
        review.setUserId(requestDto.getUserId());
        
        return reviewRepository.save(review);
    }
    
    // 리뷰 수정
    public Review updateReview(Long reviewId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        
        if (requestDto.getTitle() != null) {
            review.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            review.setContent(requestDto.getContent());
        }
        if (requestDto.getRating() != null) {
            review.setRating(requestDto.getRating());
        }
        if (requestDto.getTargetType() != null) {
            review.setTargetType(requestDto.getTargetType());
        }
        if (requestDto.getTargetName() != null) {
            review.setTargetName(requestDto.getTargetName());
        }
        if (requestDto.getImageUrl() != null) {
            review.setImageUrl(requestDto.getImageUrl());
        }
        
        return reviewRepository.save(review);
    }
    
    private ContentResponseDto convertToContentDto(Content content) {
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
