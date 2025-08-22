package com.springboot.lococo.userai.service;

import com.springboot.lococo.userai.dto.SurveyRequestDto;
import com.springboot.lococo.userai.model.FestivalRecommendation;
import com.springboot.lococo.userai.repository.FestivalRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FestivalAiService {

    private final GeminiService geminiService;
    private final FestivalRecommendationRepository repository;

    public FestivalAiService(GeminiService geminiService,
                             FestivalRecommendationRepository repository) {
        this.geminiService = geminiService;
        this.repository = repository;
    }

    // 1. AI 추천 + DB 저장
    public List<FestivalRecommendation> generateAndSaveFestivals(SurveyRequestDto survey) throws Exception {
        String aiResponse = geminiService.getRawFestivalResponse(survey);

        List<FestivalRecommendation> recommendations = parseAiResponse(aiResponse);

        // DB 저장
        repository.saveAll(recommendations);

        return recommendations;
    }

    private List<FestivalRecommendation> parseAiResponse(String aiResponse) {
        List<FestivalRecommendation> list = new ArrayList<>();
        String[] festivalTexts = aiResponse.split("추천 축제");

        for (int i = 1; i < festivalTexts.length && list.size() < 5; i++) {
            String text = festivalTexts[i].trim();
            if (text.isEmpty()) continue;

            // 제목과 description 분리
            String[] lines = text.split("\n", 2);
            String rawTitle = lines[0].trim();
            String description = lines.length > 1 ? lines[1].trim() : "";

            // 1️⃣ title 전처리
            String title = rawTitle.replace("(가상):", "").replace("**", "").trim();

            // 2️⃣ summary 생성 (앞 100자)
            String summary = description.length() > 100 ? description.substring(0, 100) + "..." : description;

            // 3️⃣ location, date 추출 (정규식 활용)
            String location = extractPattern(description, "위치:\\s*(.+)");
            String date = extractPattern(description, "기간:\\s*(.+)");

            // 4️⃣ keywords 추출 (간단하게 포함된 키워드)
            List<String> keywords = new ArrayList<>();
            if (description.contains("전통")) keywords.add("전통");
            if (description.contains("음악")) keywords.add("음악");
            if (description.contains("음식")) keywords.add("음식");

            FestivalRecommendation fr = FestivalRecommendation.builder()
                    .title(title)
                    .description(description)
                    .summary(summary)
                    .location(location)
                    .date(date)
                    .keywords(String.join(", ", keywords))
                    .build();

            list.add(fr);
        }

        return list;
    }

    // 정규식 추출 헬퍼
    private String extractPattern(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    // 리스트 조회
    public List<FestivalRecommendation> getFestivalList() {
        return repository.findAll();
    }

    // 상세 조회
    public Optional<FestivalRecommendation> getFestivalDetail(Long id) {
        return repository.findById(id);
    }
}
