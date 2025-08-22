package com.springboot.lococo.journey.controller;

import com.springboot.lococo.journey.dto.SchedulePublicRequestDto;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.service.PublicJourneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/journey")
@RequiredArgsConstructor
public class SchedulePublicController {

    private final PublicJourneyService publicJourneyService;

    // 비회원용 여정 생성
    @PostMapping("/create")
    public ResponseEntity<?> generateJourney(@RequestBody SchedulePublicRequestDto requestDto) {
        try {
            ScheduleResponseDto response = publicJourneyService.generateJourneyForNonMember(requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 저장된 여정 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ScheduleResponseDto>> getJourneyList() {
        List<ScheduleResponseDto> schedules = publicJourneyService.getScheduleList();
        return ResponseEntity.ok(schedules);
    }
}
