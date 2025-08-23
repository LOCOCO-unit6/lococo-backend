package com.springboot.lococo.userai.controller;

import com.springboot.lococo.survey.model.UserSurvey;
import com.springboot.lococo.survey.repository.UserSurveyRepository;
import com.springboot.lococo.userai.model.FestivalRecommendation;
import com.springboot.lococo.userai.service.FestivalAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/ai/festival")
@RequiredArgsConstructor
public class FestivalAiController {

    private final FestivalAiService festivalAiService;
    private final UserSurveyRepository surveyRepository;

    // AI 기반 추천 요청
    @PostMapping("/recommend")
    public ResponseEntity<?> recommendFestivals(@RequestBody Map<String, Long> request) {
        Long surveyId = request.get("surveyId");

        if (surveyId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "surveyId is required"));
        }

        return surveyRepository.findById(surveyId)
                .map(survey -> {
                    try {
                        List<FestivalRecommendation> recommendations = festivalAiService.generateAndSaveFestivals(survey);
                        return ResponseEntity.ok(recommendations);
                    } catch (Exception e) {
                        return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
                    }
                })
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Survey not found")));
    }


    // 리스트 조회
    @GetMapping("/list")
    public List<FestivalRecommendation> getFestivalList() {
        return festivalAiService.getFestivalList();
    }

    // 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<FestivalRecommendation> getFestivalDetail(@PathVariable Long id) {
        return festivalAiService.getFestivalDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
