package com.springboot.content.dto;

import com.springboot.content.model.ContentEntity;
import lombok.Getter;

@Getter
public class ContentResponseDto {
    private final Long id; // ✅ ID 필드가 존재합니다.
    private final String title;
    private final String text;

    // ✅ 이 생성자가 바로 '어떻게'에 대한 해답입니다.
    // Entity를 받아서 필요한 데이터(id, title, text)만 뽑아내는 역할을 합니다.
    public ContentResponseDto(ContentEntity entity) {
        this.id = entity.getId();

        this.title = entity.getTitle();
        this.text = entity.getContent();
    }
}
