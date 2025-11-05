package com.springboot.lococo.content.service;


import com.springboot.lococo.content.dto.ContentCreateDto;
import com.springboot.lococo.content.dto.ContentUpdateDto;
import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.content.repository.ContentRepository;
import com.springboot.lococo.model.User;
import com.springboot.lococo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {

    private final ContentRepository contentRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

    //글 생성
    public ContentEntity addContent(Long userId, ContentCreateDto contentCreateDto, MultipartFile imageFile) throws IOException {

        log.info("✅ userId: {}", userId);
        log.info("✅ dto: {}", contentCreateDto);
        log.info("✅ imageFile: {}", imageFile != null ? imageFile.getOriginalFilename() : "null");

        // 1. 현재 사용자 (User) 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

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
                .category(contentCreateDto.getCategory())
                .user(user)
                .build();
        return contentRepository.save(contentEntity);
    }
    @Transactional
    public ContentEntity updateContent(Long userId, Long id, ContentUpdateDto contentUpdateDto, MultipartFile newImageFile) throws IOException {

        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다."));

        if (!contentEntity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다. (작성자 불일치)");
        }

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

    public void deleteContent(Long userId, Long id) {

        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다."));

        if (!contentEntity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다. (작성자 불일치)");
        }

        // 2. S3 이미지 삭제: ContentEntity에 저장된 URL을 사용해 이미지를 삭제합니다.
        if (contentEntity.getImageUrl() != null) {
            imageService.deleteImageFromS3(contentEntity.getImageUrl());
        }
        contentRepository.deleteById(id);
    }

    //컨텐츠 전체 조회
    public List<ContentEntity> findAll() {
        return contentRepository.findAll();
    }

    //컨텐츠 사용자별 조회
    public List<ContentEntity> getMyContents(Long currentUserId) {
        // 이 한 줄의 코드가 내부적으로 'Content 테이블'과 'User 테이블'을 조인하고
        // WHERE user_id = currentUserId 조건을 적용합니다.
        return contentRepository.findByUserId(currentUserId);
    }
}
