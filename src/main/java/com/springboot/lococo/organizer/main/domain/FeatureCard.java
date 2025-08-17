package com.springboot.lococo.organizer.main.domain;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class FeatureCard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;
    private String title;
    private String description;
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrganizerMain main;
}