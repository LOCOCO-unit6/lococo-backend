package com.springboot.lococo.journey.controller;

import com.springboot.lococo.journey.dto.ScheduleRequestDto;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/ai/journey")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

   // 일정 생성
    @PostMapping("/create")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        ScheduleResponseDto responseDto = scheduleService.generateSchedule(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // AI 일정 추천 결과 조회
    @GetMapping("/list")
    public ResponseEntity<List<ScheduleResponseDto>> getScheduleList() {
        List<ScheduleResponseDto> schedules = scheduleService.getScheduleList();
        return ResponseEntity.ok(schedules);
    }
}
