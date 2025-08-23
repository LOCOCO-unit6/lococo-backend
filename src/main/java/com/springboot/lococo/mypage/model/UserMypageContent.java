package com.springboot.lococo.mypage.model;

import com.springboot.lococo.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_mypage_content")
@Getter
@Setter
@NoArgsConstructor
public class UserMypageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String region;        // 지역 추가
    private String description;   // 소개 추가
    private String imageUrl;      // 이미지 URL 추가

    private boolean favorite;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
