package com.springboot.lococo.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromotionResponseDto {
    private List<InstagramPostResponseDto> instagramPosts;
    private List<BlogPostResponseDto> blogPosts;
}