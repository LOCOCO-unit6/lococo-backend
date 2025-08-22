package com.springboot.lococo.userai.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FestivalDto {
    private String id;                // AI가 만들어주는 임시 ID 혹은 DB ID
    private String name;
    private String city;              // 도시/시군구
    private String region;            // 광역/도
    private LocalDate startDate;
    private LocalDate endDate;
    private String venue;
    private String description;       // 간단 설명
    private String reason;            // 추천 이유
    private List<String> tags;        // ["음식","전통","음악"]
    private String imageUrl;          // 카드를 위한 대표 이미지(없으면 null)
    private String priceRange;        // "무료", "일부 유료", "유료"
}
