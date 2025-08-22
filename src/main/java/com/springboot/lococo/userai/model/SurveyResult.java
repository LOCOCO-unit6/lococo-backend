package com.springboot.lococo.userai.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_result")
@Getter @Setter
@NoArgsConstructor
public class SurveyResult {

    @Id
    @UuidGenerator
    @Column(length = 36)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestJson;       // 설문 원본 JSON

    @Lob
    @Column(columnDefinition = "TEXT")
    private String resultJson;        // AI 추천 리스트 JSON

    private LocalDateTime createdAt = LocalDateTime.now();
}
