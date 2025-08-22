package com.springboot.lococo.aiplanner.dto;


import lombok.Builder; import lombok.Data;

@Data @Builder
public class ProposalDraftResponse {
    private String sessionId;
    private String title;             // 제안서 제목
    private String overview;          // 축제 개요(마크다운/텍스트)
    private String intentAndConcept;  // 기획 의도 및 컨셉
    private String program;           // 프로그램 구성
    private String sideEvents;        // 참여/부대 행사
    private String expectedEffects;   // 기대효과
    private String region;
    private String season;            // 가공된 대표 시즌 문자열
    private String target;
}