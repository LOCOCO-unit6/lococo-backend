package com.springboot.lococo.organizermypage.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Content {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;            // 콘텐츠 제목
    @Enumerated(EnumType.STRING)
    private ContentType type;        // POSTER / INSTAGRAM / BLOG
    private String thumbnailUrl;     // 썸네일(선택)
    private String linkUrl;          // 상세 이동 링크(인스타/블로그 등)
    private String affiliation;      // 소속 기준

    @CreationTimestamp
    private LocalDateTime createdAt;
}