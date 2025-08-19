package com.springboot.lococo.aiplanner.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlannerInputRequest {
    private String sessionId;          // 세션 ID
    private String region;             // 지역 (예: "용인 기흥구")
    private List<String> seasons;      // ["봄","여름","가을","겨울"] 중 택1+ (또는 "상관없음")
    private String target;             // 타깃 (예: "20대 대학생, 남녀 혼합")
    private String localSpecialties;   // 지역 특산물 (없으면 빈문자)
    private String brief;              // 간단 설명 (없으면 빈문자)
}