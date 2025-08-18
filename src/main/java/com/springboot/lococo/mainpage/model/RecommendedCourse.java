package com.springboot.lococo.mainpage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommended_courses")
@Data
public class RecommendedCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String destination;
    
    @Column(nullable = false)
    private String category; // 여행지, 맛집, 액티비티 등
    
    @Column(nullable = false)
    private Integer duration; // 소요 시간 (일)
    
    @Column(nullable = false)
    private Integer difficulty; // 난이도 (1-5)
    
    @Column
    private String imageUrl;
    
    @Column
    private String mapUrl;
    
    @Column(nullable = false)
    private Integer viewCount; // 조회수
    
    @Column(nullable = false)
    private Integer likeCount; // 좋아요 수
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
