package com.springboot.lococo.userai.controller;

import com.springboot.lococo.userai.dto.SurveyRequestDto;
import com.springboot.lococo.userai.model.FestivalRecommendation;
import com.springboot.lococo.userai.service.FestivalAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/ai/festival")
public class FestivalAiController {

    private final FestivalAiService festivalAiService;



    public FestivalAiController(FestivalAiService festivalAiService) {
        this.festivalAiService = festivalAiService;
    }

    // AI 추천 + DB 저장
    @PostMapping("/survey")
    public Map<String, Object> recommendBySurvey(@RequestBody SurveyRequestDto survey) throws Exception {
        List<FestivalRecommendation> saved = festivalAiService.generateAndSaveFestivals(survey);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "추천 완료!");
        response.put("data", saved);

        return response;
    }

    // 리스트 조회
    @GetMapping("/list")
    public List<Map<String, Object>> getFestivalList() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (var f : festivalAiService.getFestivalList()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("title", f.getTitle());
            map.put("summary", f.getDescription() != null
                    ? f.getDescription().substring(0, Math.min(50, f.getDescription().length()))
                    : "");
            result.add(map);
        }
        return result;
    }


    // 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<FestivalRecommendation> getFestivalDetail(@PathVariable Long id) {
        return festivalAiService.getFestivalDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
