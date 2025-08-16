package com.springboot.login_back.user.mypage.service;

import com.springboot.login_back.model.User;
import com.springboot.login_back.repository.UserRepository;
import com.springboot.login_back.user.mainpage.dto.ReviewResponseDto;
import com.springboot.login_back.user.mypage.dto.ContentResponseDto;
import com.springboot.login_back.user.mypage.dto.JourneyResponseDto;
import com.springboot.login_back.user.mypage.dto.ReviewRequestDto;
import com.springboot.login_back.user.mypage.dto.UserUpdateRequestDto;
import com.springboot.login_back.user.mypage.model.Content;
import com.springboot.login_back.user.mypage.model.Journey;
import com.springboot.login_back.user.mypage.model.Review;
import com.springboot.login_back.user.mypage.repository.ContentRepository;
import com.springboot.login_back.user.mypage.repository.JourneyRepository;
import com.springboot.login_back.user.mypage.repository.ReviewRepository;
import com.springboot.login_back.user.mypage.repository.UserFavoriteContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserFavoriteContentRepository userFavoriteContentRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원정보 수정
    public User updateUserInfo(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (requestDto.getIdentification() != null) user.setIdentification(requestDto.getIdentification());
        if (requestDto.getEmail() != null) user.setEmail(requestDto.getEmail());
        if (requestDto.getPhoneNumber() != null) user.setPhoneNumber(requestDto.getPhoneNumber());

        if(requestDto.getPassword() != null) {
            if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
                throw new RuntimeException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }


        return userRepository.save(user);
    }

//    // 콘텐츠 모아보기
//    @Transactional(readOnly = true)
//    public List<ContentResponseDto> getAllContents() {
//        return contentRepository.findAll()
//                .stream()
//                .map(content -> {
//                    ContentResponseDto dto = new ContentResponseDto();
//                    dto.setId(content.getId());
//                    dto.setTitle(content.getTitle());
//                    dto.setDescription(content.getDescription());
//                    dto.setType(content.getType());
//                    dto.setLocation(content.getLocation());
//                    dto.setImageUrl(content.getImageUrl());
//                    dto.setCreatedAt(content.getCreatedAt());
//                    dto.setUpdatedAt(content.getUpdatedAt());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }


    @Transactional(readOnly = true)
    public List<ContentResponseDto> getFavoriteContents(Long userId) {
        return userFavoriteContentRepository.findByUserId(userId)
                .stream()
                .map(fav -> {
                    Content content = fav.getContent();
                    ContentResponseDto dto = new ContentResponseDto();
                    dto.setId(content.getId());
                    dto.setTitle(content.getTitle());
                    dto.setType(content.getType());
                    dto.setLocation(content.getLocation());
                    dto.setImageUrl(content.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }



    // 현재 여정 조회
    @Transactional(readOnly = true)
    public JourneyResponseDto getCurrentJourney() {
        Journey journey = journeyRepository
                .findFirstByStatusOrderByStartDateDesc(Journey.JourneyStatus.ONGOING)
                .orElseThrow(() -> new RuntimeException("진행 중인 여정이 없습니다."));

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

    // 개인 리뷰 조회 (마이페이지용)
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getMyReviews(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(review -> {
                    ReviewResponseDto dto = new ReviewResponseDto();
                    dto.setCreatedAt(review.getCreatedAt());
                    dto.setTitle(review.getTitle());
                    dto.setRating(review.getRating());
                    dto.setContent(review.getContent());
                    dto.setLocation(review.getLocation());
                    dto.setImageUrls(review.getImageUrls());
                    dto.setRecommendation(review.getRecommendation());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // 리뷰 작성
    public Review createReview(ReviewRequestDto requestDto) {
        Review review = new Review();
        review.setTitle(requestDto.getTitle());
        review.setContent(requestDto.getContent());
        review.setRating(requestDto.getRating());
        review.setLocation(requestDto.getLocation());
        review.setImageUrls(requestDto.getImageUrls());
        review.setRecommendation(requestDto.getRecommendation());
        review.setUserId(requestDto.getUserId());
        review.setTargetType(requestDto.getTargetType());
        review.setTargetName(requestDto.getTargetName());
        return reviewRepository.save(review);
    }

    // 리뷰 수정
    public Review updateReview(Long reviewId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (requestDto.getTitle() != null) review.setTitle(requestDto.getTitle());
        if (requestDto.getContent() != null) review.setContent(requestDto.getContent());
        if (requestDto.getRating() != null) review.setRating(requestDto.getRating());
        if (requestDto.getLocation() != null) review.setLocation(requestDto.getLocation());
        if (requestDto.getImageUrls() != null) review.setImageUrls(requestDto.getImageUrls());
        if (requestDto.getRecommendation() != null) review.setRecommendation(requestDto.getRecommendation());
        if (requestDto.getTargetType() != null) review.setTargetType(requestDto.getTargetType());
        if (requestDto.getTargetName() != null) review.setTargetName(requestDto.getTargetName());

        return reviewRepository.save(review);
    }
}
