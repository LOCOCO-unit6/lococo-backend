package com.springboot.lococo.journey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeminiApiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Gemini에 프롬프트를 전달하고 응답을 받아서 순수 텍스트만 추출합니다.
     *
     * @param prompt 사용자의 요청 내용
     * @return Gemini가 생성한 콘텐츠 (텍스트 형태)
     */
    public String generateContent(String prompt) {
        String fullUrl = buildFullApiUrl();

        HttpHeaders headers = buildHeaders();
        String body = buildRequestBody(prompt);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);
            return extractTextFromResponse(response.getBody());
        } catch (Exception e) {
            // 로깅 및 추후 고도화를 고려해 에러 메시지는 사용자용으로 최소화
            e.printStackTrace();
            return "AI 응답 생성 중 오류가 발생했습니다.";
        }
    }

    /**
     * API URL에 key를 붙여 최종 호출 주소 생성
     */
    private String buildFullApiUrl() {
        return apiUrl + "?key=" + apiKey;
    }

    /**
     * HTTP 요청 헤더 구성
     */
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Gemini API 요청 포맷에 맞춘 JSON 생성
     */
    private String buildRequestBody(String prompt) {
        return String.format("""
            {
              "contents": [
                {
                  "parts": [
                    { "text": "%s" }
                  ]
                }
              ]
            }
            """, prompt.replace("\"", "\\\"")); // 혹시 모를 따옴표 이스케이프 처리
    }

    /**
     * Gemini 응답 JSON에서 순수 텍스트 추출
     */
    private String extractTextFromResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        return root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();
    }
}
