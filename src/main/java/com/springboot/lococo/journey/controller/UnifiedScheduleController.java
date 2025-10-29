package com.springboot.lococo.journey.controller;

import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.dto.UserScheduleRequestDto;
import com.springboot.lococo.journey.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/schedules")
@RequiredArgsConstructor
public class UnifiedScheduleController {

    private final ScheduleService scheduleService;

    // 사용자 직접 일정 생성
    @PostMapping("/create")
    public ResponseEntity<ScheduleResponseDto> createUserSchedule(
            @RequestParam Long userId,
            @RequestBody UserScheduleRequestDto requestDto) {
        ScheduleResponseDto responseDto = scheduleService.createUserSchedule(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 통합 일정 목록 조회 (AI + 사용자 생성)
    @GetMapping("/all")
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        List<ScheduleResponseDto> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    // 사용자별 일정 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleResponseDto>> getUserSchedules(@PathVariable Long userId) {
        List<ScheduleResponseDto> schedules = scheduleService.getUserSchedules(userId);
        return ResponseEntity.ok(schedules);
    }

    // 특정 일정 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long scheduleId) {
        // TODO: 개별 일정 조회 메서드 구현 필요
        return ResponseEntity.ok().build();
    }
}
