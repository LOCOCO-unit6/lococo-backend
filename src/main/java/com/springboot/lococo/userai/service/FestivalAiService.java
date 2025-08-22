package com.springboot.lococo.userai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lococo.survey.model.UserSurvey;
import com.springboot.lococo.userai.model.FestivalRecommendation;
import com.springboot.lococo.userai.repository.FestivalRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FestivalAiService {

    private final GeminiService geminiService;
    private final FestivalRecommendationRepository repository;

    public FestivalAiService(GeminiService geminiService,
                             FestivalRecommendationRepository repository) {
        this.geminiService = geminiService;
        this.repository = repository;
    }

    // AI 추천 + DB 저장
    public List<FestivalRecommendation> generateAndSaveFestivals(UserSurvey survey) {
        if (survey == null) return new ArrayList<>();

        try {
            String aiResponse = geminiService.getRawFestivalResponse(survey);
            List<FestivalRecommendation> recommendations = parseAiResponse(aiResponse);

            // DB 저장
            repository.saveAll(recommendations);

            return recommendations;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<FestivalRecommendation> parseAiResponse(String aiResponse) {
        List<FestivalRecommendation> list = new ArrayList<>();
        if (aiResponse == null || aiResponse.isEmpty()) return list;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(aiResponse);

            if (!root.isArray()) return list;

            for (JsonNode node : root) {
                FestivalRecommendation fr = FestivalRecommendation.builder()
                        .title(node.path("title").asText(""))
                        .location(node.path("location").asText("미정"))
                        .date(node.path("date").asText("미정"))
                        .price(node.path("price").asText("미정"))
                        .image(node.path("image").asText(""))
                        .description(node.path("description").asText(""))
                        .build();
                list.add(fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // JSON 파싱 실패 시 빈 리스트 반환
        }

        return list;
    }

    public List<FestivalRecommendation> getFestivalList() {
        return repository.findAll();
    }

    public Optional<FestivalRecommendation> getFestivalDetail(Long id) {
        return repository.findById(id);
    }
}
