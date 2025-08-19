package com.springboot.lococo.userai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/ai/festival")
@RequiredArgsConstructor
public class FestivalAiController {

    private final GeminiService geminiService;


    @PostMapping("/preference")
    public ResponseEntity<String> recommendFestivalByPost(@RequestBody PreferenceRequestDto dto) {
        String prompt = "사용자 취향: " + dto.getPreferences() +
                "\n이 취향에 맞는 지역 축제를 3개 추천해줘. " +
                "각 축제는 이름, 간단한 설명, 추천 이유를 포함해줘.";
        return ResponseEntity.ok(geminiService.getFestivalRecommendation(prompt));
    }


    @GetMapping
    public ResponseEntity<String> recommendFestival(@RequestParam String preferences) {
        String prompt = "사용자 취향: " + preferences +
                "\n이 취향에 맞는 지역 축제를 3개 추천해줘. " +
                "각 축제는 이름, 간단한 설명, 추천 이유를 포함해줘.";
        return ResponseEntity.ok(geminiService.getFestivalRecommendation(prompt));
    }


    @GetMapping("/list")
    public ResponseEntity<String> getFestivalList(@RequestParam String preferences) {
        String prompt = "사용자 취향: " + preferences +
                "\n이 취향에 맞는 축제 리스트를 JSON 배열 형태로 제공해줘. " +
                "예: [{\"name\":\"OO축제\",\"description\":\"...\",\"reason\":\"...\"}]";
        return ResponseEntity.ok(geminiService.getFestivalRecommendation(prompt));
    }
}
