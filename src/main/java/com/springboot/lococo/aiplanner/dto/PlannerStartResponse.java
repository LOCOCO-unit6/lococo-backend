package com.springboot.lococo.aiplanner.dto;


import lombok.AllArgsConstructor; import lombok.Data;

@Data @AllArgsConstructor
public class PlannerStartResponse {
    private String sessionId;   // UUID
    private String message;     // "세션 생성 완료"
}