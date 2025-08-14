package com.springboot.content.dto;

import com.springboot.content.model.ContentEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentCreateDto {
    private String title;
    private String text;

    public ContentCreateDto(ContentEntity entity) {

        this.title = entity.getTitle();
        this.text = entity.getContent();
    }


}
