package com.springboot.lococo.promotion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "promotions")
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contentId", nullable = false)
    private Long contentId;

    @Column(name = "blogPost", nullable = false, length = 5000)
    private String blogPost;
    @Column(name = "instagramPost", nullable = false, length = 5000)
    private String instagramPost;
    @Column(name = "poster", nullable = false, length = 5000)
    private String poster;
}
