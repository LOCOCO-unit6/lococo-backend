package com.springboot.lococo.organizer.main.domain;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity(name = "OrganizerMainReview")
@Table(name = "organizer_review",
        indexes = @Index(name = "idx_organizer_review_createdAt", columnList = "createdAt DESC"))
public class OrganizerMainReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private Integer rating;

    @Lob
    private String comment;

    private LocalDateTime createdAt;
    private String source;
    private Long festivalId;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}