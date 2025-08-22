package com.springboot.lococo.userai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lococo.userai.config.GeminiConfig;
import com.springboot.lococo.userai.dto.SurveyRequestDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GeminiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GeminiConfig config;

    public GeminiService(GeminiConfig config) {
        this.config = config;
    }

    // AI 호출 후 원시 문자열 반환
    public String getRawFestivalResponse(SurveyRequestDto survey) throws Exception {
        String prompt = buildPrompt(survey);

        String requestBody = """
        {
          "contents": [
            {
              "parts": [
                {"text": "%s"}
              ]
            }
          ]
        }
        """.formatted(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = config.getApiUrl() + "?key=" + config.getApiKey();
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode candidates = root.path("candidates");

        if (candidates.isEmpty() || !candidates.get(0).has("content")) {
            return "";
        }

        return candidates.get(0)
                .path("content").path("parts").get(0).path("text").asText();
    }

    private String buildPrompt(SurveyRequestDto survey) {
        return String.format("추천 축제 조건:\n위치: %s\n기간: %s ~ %s\n키워드: %s\n연령대: %s\n동반자: %s\n여행스타일: %s\n일일예산: %d원\n가야할 곳: %s\n참고 메모: %s\n이미지 힌트: %s",
                survey.getLocation(),
                survey.getStartDate(), survey.getEndDate(),
                String.join(", ", survey.getKeywords()),
                survey.getAgeRange(),
                String.join(", ", survey.getCompanions()),
                survey.getTravelStyle(),
                survey.getDailyBudget(),
                String.join(", ", survey.getMustVisit()),
                survey.getNote(),
                survey.getImageHints() != null ? String.join(", ", survey.getImageHints()) : "없음"
        );
    }
}

