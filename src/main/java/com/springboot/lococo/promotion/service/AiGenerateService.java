package com.springboot.lococo.promotion.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class AiGenerateService {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateContent(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullApiUrl = apiUrl + "?key=" + apiKey;

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> contents = Map.of("parts", Collections.singletonList(part));
        Map<String, Object> requestBody = Map.of("contents", Collections.singletonList(contents));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullApiUrl, requestEntity, String.class);
            String rawResponse = responseEntity.getBody();

            // 💡 응답 문자열에서 마크다운 문자를 제거하는 로직 추가
            String cleanJsonString = rawResponse.replaceAll("```json", "").replaceAll("```", "").trim();

            JsonNode root = objectMapper.readTree(cleanJsonString);
            String generatedText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            return generatedText;

        } catch (Exception e) {
            System.err.println("AI 응답 생성 실패: " + e.getMessage());
            e.printStackTrace();
            return "AI 응답 생성에 실패했습니다.";
        }
    }
}