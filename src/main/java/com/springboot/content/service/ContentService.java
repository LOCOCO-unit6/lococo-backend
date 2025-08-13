package com.springboot.content.service;

import com.springboot.content.dto.ContentCreateDto;
import com.springboot.content.dto.ContentUpdateDto;
import com.springboot.content.model.ContentEntity;
import com.springboot.content.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final ContentRepository contentRepository;


    //글 생성
    public ContentEntity addText(ContentCreateDto contentCreateDto) {
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setTitle(contentCreateDto.getTitle());
        contentEntity.setContent(contentCreateDto.getText());
        return contentRepository.save(contentEntity);
    }

    @Transactional
    public ContentEntity updateContent(Long id, ContentUpdateDto contentUpdateDto) {
        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));

        contentEntity.setTitle(contentUpdateDto.getTitle());
        contentEntity.setContent(contentUpdateDto.getText());

        return contentEntity;
    }

    public void deleteText(Long Id) {

        contentRepository.deleteById(Id);
    }


}
