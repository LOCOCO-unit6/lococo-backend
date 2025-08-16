package com.springboot.login_back.user.mypage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "journeys")
@Data
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false) private String destination;
    @Column(nullable = false) private LocalDateTime startDate;
    @Column(nullable = false) private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JourneyStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum JourneyStatus {
        PLANNING, ONGOING, COMPLETED, CANCELLED
    }
}
