package com.springboot.lococo.journey.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TravelSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;        // 일정 날짜
    private String location;       // 지역 (예: "서울 종로구")

    private String title;          // 여정 이름 (예: "종로 전통 문화 탐방")
    private String summary;        // 짧은 세부정보 (예: "경복궁과 북촌 한옥마을 중심의 당일 코스")

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TravelActivity> activities = new ArrayList<>();
}

