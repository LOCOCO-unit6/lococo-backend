package com.springboot.lococo.content.model;

import com.springboot.lococo.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class ContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //기본정보
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "organizer", nullable = false)
    private String organizer;

    @Column(name = "category", nullable = false)
    private String category;


    //콘텐츠 상세
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    //사진
    @Column(name = "imageUrl", nullable = true, columnDefinition = "TEXT")
    private String imageUrl;

    @Builder
    public ContentEntity(String name, String location, LocalDate startDate,
                         LocalDate endDate, String organizer, String title,
                         String text, String imageUrl,String category, User user) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organizer = organizer;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.category = category;
        this.user = user;
        //사진
    }

    /*join관계 설정*/
    //User 테이블과 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
