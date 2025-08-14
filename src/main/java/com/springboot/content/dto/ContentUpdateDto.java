package com.springboot.content.dto;

import com.springboot.content.model.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentUpdateDto {

    String title;
    String text;

    public ContentUpdateDto(ContentEntity entity) {
        this.title = entity.getTitle();
        this.text = entity.getContent();
    }
}
