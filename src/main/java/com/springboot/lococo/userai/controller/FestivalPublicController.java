package com.springboot.lococo.userai.controller;

import com.springboot.lococo.userai.model.FestivalRecommendation;
import com.springboot.lococo.userai.repository.FestivalRecommendationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/festival")
public class FestivalPublicController {

    private final FestivalRecommendationRepository repository;

    public FestivalPublicController(FestivalRecommendationRepository repository) {
        this.repository = repository;
    }

    // 1️⃣ 리스트 (비회원용 추천, 필터 지원)
    @GetMapping("/list")
    public List<FestivalRecommendation> getFestivals(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String keyword
    ) {
        return repository.findAll().stream()
                .filter(f -> location == null || (f.getLocation() != null && f.getLocation().contains(location)))
                .filter(f -> keyword == null || (f.getKeywords() != null && f.getKeywords().contains(keyword)))
                .toList();
    }

    // 2️⃣ 상세 조회
    @GetMapping("/detail/{id}")
    public FestivalRecommendation getFestivalDetail(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festival not found: " + id));
    }
}
