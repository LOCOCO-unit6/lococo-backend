package com.springboot.lococo.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.lococo.mypage.model.UserMypageContent;
import lombok.Getter;

@Getter
public class ContentResponseDto {
    private Long id;

    private String title;

    @JsonProperty("region") // JSON에서 region으로 노출
    private String region;

    @JsonProperty("description") // JSON에서 description으로 노출
    private String description;

    @JsonProperty("imageUrl") // JSON에서 imageUrl로 노출
    private String imageUrl;

    @JsonProperty("favorite") // JSON에서 favorite으로 노출
    private boolean favorite;

    public ContentResponseDto(UserMypageContent content) {
        this.id = content.getId();
        this.title = content.getTitle();
        this.region = content.getRegion();
        this.description = content.getDescription();
        this.imageUrl = content.getImageUrl();
        this.favorite = content.isFavorite();
    }
}
