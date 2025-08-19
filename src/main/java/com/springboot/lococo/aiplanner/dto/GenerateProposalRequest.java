package com.springboot.lococo.aiplanner.dto;


import lombok.Data;

@Data
public class GenerateProposalRequest {
    private String sessionId;  // 필수
    // 필요 시 온더플라이로 값 덮어쓰기 용
    private String region;
    private String target;
}