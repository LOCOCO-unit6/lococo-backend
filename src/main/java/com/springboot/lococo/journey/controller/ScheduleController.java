package com.springboot.lococo.journey.controller;

import com.springboot.lococo.journey.dto.ScheduleRequestDto;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/generate")
    public ResponseEntity<ScheduleResponseDto> generateSchedule(@RequestBody ScheduleRequestDto requestDto) {
        ScheduleResponseDto responseDto = scheduleService.generateSchedule(requestDto);
        return ResponseEntity.ok(responseDto);
    }


}

