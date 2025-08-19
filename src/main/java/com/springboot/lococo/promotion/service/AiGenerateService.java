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

@Service
public class AiGenerateService {
    // application.properties에서 API 키와 URL을 안전하게 주입받음
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateContent(String prompt) {
        // 1. HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // API 키를 쿼리 파라미터로 추가
        String fullApiUrl = apiUrl + "?key=" + apiKey;

        // 2. Gemini API가 요구하는 형식에 맞게 요청 본문(Body) 생성
        String requestBody = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}",
                prompt
        );

        // 3. 헤더와 본문을 합쳐서 HTTP 요청 객체 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 4. RestTemplate을 사용하여 외부 API에 POST 요청 보내기
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullApiUrl, requestEntity, String.class);

            // 5. 응답(JSON)에서 텍스트 부분만 파싱하여 추출
            JsonNode root = objectMapper.readTree(responseEntity.getBody());
            String generatedText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            return generatedText;

        } catch (Exception e) {
            // 실제 서비스에서는 로깅 등 더 정교한 예외 처리가 필요합니다.
            e.printStackTrace();
            return "AI 응답 생성에 실패했습니다.";
        }
    }
}
