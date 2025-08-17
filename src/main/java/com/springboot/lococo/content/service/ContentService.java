package com.springboot.lococo.content.service;


import com.springboot.lococo.content.dto.ContentCreateDto;
import com.springboot.lococo.content.dto.ContentUpdateDto;
import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.content.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final ImageService imageService;

    //글 생성
    public ContentEntity addContent(ContentCreateDto contentCreateDto, MultipartFile imageFile) throws IOException {

        String imageUrl = imageService.uploadImageToS3(imageFile);

        ContentEntity contentEntity = ContentEntity.builder()
                .name(contentCreateDto.getName())
                .location(contentCreateDto.getLocation())
                .startDate(contentCreateDto.getStartDate())
                .endDate(contentCreateDto.getEndDate())
                .organizer(contentCreateDto.getOrganizer())
                .title(contentCreateDto.getTitle())
                .text(contentCreateDto.getText())
                .imageUrl(imageUrl)
                .build();
        return contentRepository.save(contentEntity);
    }
    @Transactional
    public ContentEntity updateContent(Long id, ContentUpdateDto contentUpdateDto, MultipartFile newImageFile) throws IOException {
        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다."));

        if (newImageFile != null && !newImageFile.isEmpty()) {
            if (contentEntity.getImageUrl() != null) {
                imageService.deleteImageFromS3(contentEntity.getImageUrl());
            }
            String newImageUrl = imageService.uploadImageToS3(newImageFile);
            contentEntity.setImageUrl(newImageUrl);
        }

        contentEntity.setName(contentUpdateDto.getName());
        contentEntity.setLocation(contentUpdateDto.getLocation());
        contentEntity.setStartDate(contentUpdateDto.getStartDate());
        contentEntity.setEndDate(contentUpdateDto.getEndDate());
        contentEntity.setOrganizer(contentUpdateDto.getOrganizer());
        contentEntity.setTitle(contentUpdateDto.getTitle());
        contentEntity.setText(contentUpdateDto.getText());
        //contentEntity.setImageUrl(contentUpdateDto.getImageUrl());

        return contentEntity;
    }

    public void deleteContent(Long id) {

        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다."));

        // 2. S3 이미지 삭제: ContentEntity에 저장된 URL을 사용해 이미지를 삭제합니다.
        if (contentEntity.getImageUrl() != null) {
            imageService.deleteImageFromS3(contentEntity.getImageUrl());
        }
        contentRepository.deleteById(id);
    }

    public List<ContentEntity> findAll() {
        return contentRepository.findAll();
    }

}
