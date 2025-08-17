package com.springboot.lococo.user.mainpage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviewlist")
@Data
public class ReviewList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT") private String content;
    @Column(nullable = false) private Integer rating; // 1-5점
    @Column(nullable = false) private String targetType; // 여행지, 맛집, 액티비티 등
    @Column(nullable = false) private String targetName;
    private String imageUrl;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 리뷰 작성자 ID

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
