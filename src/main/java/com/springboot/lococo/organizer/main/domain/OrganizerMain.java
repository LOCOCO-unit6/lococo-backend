package com.springboot.lococo.organizer.main.domain;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class OrganizerMain {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String heroTitle;
    private String heroSubtitle;

    @Lob
    private String serviceGuide;

    @Lob
    private String trustMetrics;

    private String footerText;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "main", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<FeatureCard> featureCards = new ArrayList<>();

    @OneToMany(mappedBy = "main", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<CurationItem> curationItems = new ArrayList<>();

    public void setFeatureCards(List<FeatureCard> cards) {
        this.featureCards.clear();
        if (cards != null) cards.forEach(c -> { c.setMain(this); this.featureCards.add(c); });
    }
    public void setCurationItems(List<CurationItem> items) {
        this.curationItems.clear();
        if (items != null) items.forEach(i -> { i.setMain(this); this.curationItems.add(i); });
    }

    @PrePersist @PreUpdate
    public void touch() { this.updatedAt = LocalDateTime.now(); }
}