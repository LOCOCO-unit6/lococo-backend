package com.springboot.login_back.user.mypage.model;

import com.springboot.login_back.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserFavoriteContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Content getContent() {
        return content;
    }
}
