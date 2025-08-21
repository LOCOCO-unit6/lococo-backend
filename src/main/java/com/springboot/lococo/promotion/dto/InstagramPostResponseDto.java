package com.springboot.lococo.promotion.dto;

import com.springboot.lococo.promotion.model.InstagramPostEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class InstagramPostResponseDto {
    private Long instagramId;
    private String title;
    private String content;
    private List<String> hashtags; // List<String>으로 변경

    // Entity를 DTO로 변환하는 생성자
    public InstagramPostResponseDto(InstagramPostEntity entity) {
        this.instagramId = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.hashtags = List.of(entity.getHashtags().split(" ")); // DB에서 가져온 String을 List로 변환
    }
}