package com.springboot.lococo.aiplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.mock:false}")
    private boolean mock;

    public String generate(String prompt) {
        if (mock) {
            return """
                    # 모의 제목
                    ## 축제 개요
                    - 축제명: 모의 축제
                    - 슬로건: 모의
                    - 일정: TBD
                    - 장소: TBD
                    - 타깃: TBD

                    ## 기획 의도 및 컨셉
                    모의 데이터

                    ## 프로그램 구성
                    - A
                    - B

                    ## 참여 및 부대 행사
                    - C

                    ## 기대효과
                    - D
                    """;
        }

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> res = restTemplate.exchange(
                    apiUrl + "?key=" + apiKey, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

            Map<String, Object> resp = res.getBody();
            if (resp == null) throw new IllegalArgumentException("Gemini 응답이 비어 있습니다.");

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) resp.get("candidates");
            if (candidates == null || candidates.isEmpty())
                throw new IllegalArgumentException("Gemini 응답에 candidates가 없습니다: " + resp);

            Map<String, Object> contentObj = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = contentObj == null ? List.of()
                    : (List<Map<String, Object>>) contentObj.get("parts");

            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> p : parts) {
                Object t = p.get("text");
                if (t != null) sb.append(t.toString());
            }
            String out = sb.toString().trim();
            if (out.isEmpty()) throw new IllegalArgumentException("Gemini 응답이 비어 있습니다(parts.text).");
            return out;

        } catch (HttpStatusCodeException e) {
            // ApiExceptionHandler에서 502로 변환되어 내려감
            throw e;
        } catch (ResourceAccessException e) {
            throw new IllegalArgumentException("Gemini 호출 실패(네트워크/타임아웃): " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Gemini 응답 파싱 실패: " + e.getMessage());
        }
    }
}