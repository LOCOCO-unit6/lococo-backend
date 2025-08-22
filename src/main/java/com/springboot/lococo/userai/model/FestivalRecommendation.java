package com.springboot.lococo.userai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "festival_recommendation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalRecommendation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;        // 축제 이름
    private String summary;      // 리스트에서 보여줄 간단 설명
    @Column(length = 5000)
    private String description;  // 상세 내용
    private String location;     // 위치
    private String date;         // 기간
    private String keywords;     // 키워드
}
