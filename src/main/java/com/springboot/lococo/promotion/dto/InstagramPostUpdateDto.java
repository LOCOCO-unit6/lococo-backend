package com.springboot.lococo.promotion.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class InstagramPostUpdateDto {
    private Long id;
    private String title;
    private String content;
    private String hashtags;
}