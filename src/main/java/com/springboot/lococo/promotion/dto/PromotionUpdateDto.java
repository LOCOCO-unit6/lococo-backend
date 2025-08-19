package com.springboot.lococo.promotion.dto;


import com.springboot.lococo.content.model.ContentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PromotionUpdateDto {

    private String instagramPost;
    private String blogPost;
    private String poster;


}
