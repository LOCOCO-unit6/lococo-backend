package com.springboot.lococo.userai;

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

    public String getFestivalRecommendation(String preferencePrompt) {
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
        """.formatted(preferencePrompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Gemini는 key를 URL 파라미터로 전달하는 게 공식 방식
        String url = config.getApiUrl() + "?key=" + config.getApiKey();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
