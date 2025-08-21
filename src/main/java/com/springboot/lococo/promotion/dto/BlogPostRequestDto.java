package com.springboot.lococo.promotion.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class BlogPostRequestDto {
    private Long contentId;
    private String additionalText;
}