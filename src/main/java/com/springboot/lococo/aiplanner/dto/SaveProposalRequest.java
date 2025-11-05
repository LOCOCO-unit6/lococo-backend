package com.springboot.lococo.aiplanner.dto;


import lombok.Data;

@Data
public class SaveProposalRequest {
    private String sessionId;        // 세션 초안 저장
    //private Long organizerId;        // 작성자(=User) ID
    private String affiliation;      // 소속 (리스트 필터링용) - 없으면 사용자에서 끌어와도 ok
}