package com.springboot.login_back.user.mypage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; // 후기글
    @Column(nullable = false)
    private Integer rating; // 1-5점

    @Column(nullable = false)
    private String targetType; // 여행지, 맛집 등

    @Column(nullable = false)
    private String targetName;

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private java.util.List<String> imageUrls; // 사진 첨부 여러개

    private String recommendation; // 추천 한마디

    private String location; // 장소

    @Column(nullable = false)
    private Long userId; // 리뷰 작성자 ID

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
