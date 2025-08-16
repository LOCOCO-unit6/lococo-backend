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
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentTitle; // 어떤 콘텐츠/행사에 달린 리뷰인지 간단 제목
    @Column(length = 2000)
    private String body;         // 리뷰 내용

    private String affiliation;  // 소속 기준(=로그인 사용자의 소속에 달린 리뷰만 보기)

    private boolean deleteRequested; // 삭제 요청 여부

    @CreationTimestamp
    private LocalDateTime createdAt;
}