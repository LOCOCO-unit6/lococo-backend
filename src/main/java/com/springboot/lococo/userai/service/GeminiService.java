package com.springboot.lococo.userai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lococo.survey.model.UserSurvey;
import com.springboot.lococo.userai.config.GeminiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final GeminiConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiService(GeminiConfig config) {
        this.config = config;
    }

    public String getRawFestivalResponse(UserSurvey survey) {
        if (survey == null) {
            logger.warn("UserSurvey is null");
            return "";
        }

        try {
            String prompt = buildPrompt(survey);
            String requestBody = String.format("""
            {
              "contents": [{"parts": [{"text": "%s"}]}]
            }
            """, prompt.replace("\"", "\\\""));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", config.getApiKey());

            String url = config.getApiUrl();
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            logger.debug("Sending request to Gemini API");
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("Gemini API request failed with status: {}", response.getStatusCode());
                return "";
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            String rawText = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText("");

            return rawText.replaceAll("(?i)```(?:json)?\s*|```", "").trim();
        } catch (Exception e) {
            logger.error("Error in getRawFestivalResponse", e);
            return "";
        }
    }

    private String buildPrompt(UserSurvey survey) {
        return String.format("""
        [요청사항]
        다음 조건에 맞는 축제를 추천해주세요.
        
        [선호 조건]
        - 위치: %s
        - 기간: %s ~ %s
        - 키워드: %s
        - 연령대: %s
        - 동반자: %s
        - 여행스타일: %s
        - 일일예산: %,d원
        - 가야할 곳: %s
        - 참고 메모: %s
        - 이미지 힌트: %s

        [응답 형식]
        반드시 JSON 배열 형식으로 추천 축제를 5개 생성해주세요.
        각 축제는 다음 필드를 포함해야 합니다:
        - title: 축제 이름 (필수)
        - location: 위치 (시/구 단위)
        - date: 날짜 (YYYY-MM-DD ~ YYYY-MM-DD)
        - price: 가격 정보 (무료/유료)
        - image: 대표 이미지 URL (없을 경우 빈 문자열)
        - description: 상세 설명 (100자 내외)

        [주의사항]
        - 반드시 유효한 JSON 형식으로 응답해주세요.
        - description 필드에는 개행 문자(\n)를 포함하지 마세요.
        - 이미지 URL이 없을 경우 빈 문자열("")로 설정해주세요.

        [응답 예시]
        [
          {
            "title": "서울 빛초롱축제",
            "location": "서울, 종로구",
            "date": "2025-09-01 ~ 2025-09-03",
            "price": "무료",
            "image": "https://example.com/festival1.jpg",
            "description": "서울의 대표적인 가을 축제로..."
          }
        ]
        """,
            getSafeString(survey.getLocation(), "없음"),
            survey.getStartDate() != null ? survey.getStartDate().toString() : "미정",
            survey.getEndDate() != null ? survey.getEndDate().toString() : "미정",
            getSafeString(String.join(", ", survey.getKeywords()), "없음"),
            getSafeString(survey.getAgeRange(), "없음"),
            getSafeString(String.join(", ", survey.getCompanions()), "없음"),
            getSafeString(survey.getTravelStyle(), "없음"),
            survey.getDailyBudget(),
            getSafeString(String.join(", ", survey.getMustVisit()), "없음"),
            getSafeString(survey.getNote(), "없음"),
            getSafeString(String.join(", ", survey.getImageHints()), "없음")
        );
    }

    private String getSafeString(String value, String defaultValue) {
        return value != null && !value.isBlank() ? value : defaultValue;
    }
}
