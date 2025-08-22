package com.springboot.lococo.userai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "festival_recommendation")
public class FestivalRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String summary;
    private String location;
    private String date;
    private String keywords;

    private String price;   // 선택적
    private String image;   // 새로 추가
}
