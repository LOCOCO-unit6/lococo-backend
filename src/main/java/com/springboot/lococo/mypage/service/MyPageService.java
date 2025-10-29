package com.springboot.lococo.mypage.service;

import com.springboot.lococo.journey.model.TravelSchedule;
import com.springboot.lococo.journey.repository.TravelScheduleRepository;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.dto.ActivityDto;
import com.springboot.lococo.model.User;
import com.springboot.lococo.mypage.dto.*;
import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.mypage.repository.ReviewRepository;
import com.springboot.lococo.mypage.repository.UserContentRepository;
import com.springboot.lococo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final UserContentRepository userContentRepository;
    private final ReviewRepository reviewRepository;
    private final TravelScheduleRepository travelScheduleRepository;

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

    public List<ContentResponseDto> getFavoriteContents() {
        return userContentRepository.findFavorites()
                .stream()
                .map(ContentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 진행 중인 일정 조회 (오늘 이후)
    public List<ScheduleResponseDto> getOngoingSchedules() {
        LocalDate today = LocalDate.now();
        return travelScheduleRepository.findOngoingSchedules(today)
                .stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
    }

    // 완료된 일정 조회 (오늘 이전)
    public List<ScheduleResponseDto> getCompletedSchedules() {
        LocalDate today = LocalDate.now();
        return travelScheduleRepository.findCompletedSchedules(today)
                .stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
    }

    // 사용자별 진행 중인 일정 조회
    public List<ScheduleResponseDto> getOngoingSchedulesByUser(Long userId) {
        LocalDate today = LocalDate.now();
        return travelScheduleRepository.findOngoingSchedulesByUser(userId, today)
                .stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
    }

    // 사용자별 완료된 일정 조회
    public List<ScheduleResponseDto> getCompletedSchedulesByUser(Long userId) {
        LocalDate today = LocalDate.now();
        return travelScheduleRepository.findCompletedSchedulesByUser(userId, today)
                .stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
    }

    // 현재 일정 (가장 가까운 진행 예정 일정)
    public ScheduleResponseDto getCurrentSchedule() {
        LocalDate today = LocalDate.now();
        List<TravelSchedule> upcoming = travelScheduleRepository.findOngoingSchedules(today);
        return upcoming.isEmpty() ? null : convertToScheduleDto(upcoming.get(0));
    }

    // 일정 삭제
    public void deleteSchedule(Long scheduleId) {
        travelScheduleRepository.deleteById(scheduleId);
    }

    // 여정 후기 작성 (TravelSchedule 기반)
    public UserReview createJourneyReview(Long travelScheduleId, ReviewRequestDto requestDto) {
        TravelSchedule travelSchedule = travelScheduleRepository.findById(travelScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("TravelSchedule not found"));
        UserReview review = new UserReview();
        review.setTravelSchedule(travelSchedule);
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

    // ===== 통합 일정 관리 메서드들 =====
    
    // 모든 일정 조회 (AI + 사용자 생성)
    public List<ScheduleResponseDto> getAllSchedules() {
        return travelScheduleRepository.findAll()
                .stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
    }

    // 사용자별 일정 조회
    public List<ScheduleResponseDto> getUserSchedules(Long userId) {
        return travelScheduleRepository.findByUser_Id(userId)
                .stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
    }

    // 특정 일정 조회
    public ScheduleResponseDto getScheduleById(Long scheduleId) {
        TravelSchedule schedule = travelScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        return convertToScheduleDto(schedule);
    }

    // TravelSchedule을 ScheduleResponseDto로 변환
    private ScheduleResponseDto convertToScheduleDto(TravelSchedule schedule) {
        List<ActivityDto> activities = schedule.getActivities().stream()
                .map(activity -> new ActivityDto(activity.getTime(), activity.getPlace()))
                .collect(Collectors.toList());

        return new ScheduleResponseDto(
                schedule.getDate().toString(),
                schedule.getLocation(),
                schedule.getTitle(),
                schedule.getSummary(),
                activities
        );
    }
}
