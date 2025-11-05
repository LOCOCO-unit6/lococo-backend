package com.springboot.lococo.promotion.model;

import com.springboot.lococo.organizermypage.model.Proposal;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "instagram_posts")
public class InstagramPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "hashtags", columnDefinition = "TEXT")
    private String hashtags;

    @Builder
    public InstagramPostEntity(Proposal proposal, String title, String content, String hashtags) {
        this.proposal = proposal;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }

    /*join관계 설정*/
    //proposal과 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false)
    private Proposal proposal;
}