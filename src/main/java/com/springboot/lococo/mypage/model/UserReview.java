package com.springboot.lococo.mypage.model;

import com.springboot.lococo.journey.model.TravelSchedule;
import com.springboot.lococo.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String location;

    private int rating;

    @Column(length = 2000)
    private String comment;

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    private String recommendation;

    private LocalDateTime createdAt = LocalDateTime.now();

    // 작성자 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // AI 여정 연결 (기존 Journey 대신 TravelSchedule 사용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_schedule_id")
    private TravelSchedule travelSchedule;
}

