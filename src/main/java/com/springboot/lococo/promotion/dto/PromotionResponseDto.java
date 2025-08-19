package com.springboot.lococo.promotion.dto;

import com.springboot.lococo.promotion.model.PromotionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PromotionResponseDto {
    private Long promotionId;
    private Long contentId;
    private String blogPost;
    private String instagramPost;
    private String poster;

    // Entity를 DTO로 변환하는 생성자
    public PromotionResponseDto(PromotionEntity entity) {
        this.promotionId = entity.getId();
        this.contentId = entity.getContentId();
        this.blogPost = entity.getBlogPost();
        this.instagramPost = entity.getInstagramPost();
        this.poster = entity.getPoster();
    }
}
