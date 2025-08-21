package com.springboot.lococo.promotion.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "blog_posts")
public class BlogPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "hashtags", columnDefinition = "TEXT")
    private String hashtags;

    @Builder
    public BlogPostEntity(Long contentId, String title, String content, String hashtags) {
        this.contentId = contentId;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }
}