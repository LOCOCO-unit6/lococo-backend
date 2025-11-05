package com.springboot.lococo.content.controller;

import com.springboot.lococo.content.dto.ContentCreateDto;
import com.springboot.lococo.content.dto.ContentResponseDto;
import com.springboot.lococo.content.dto.ContentUpdateDto;
import com.springboot.lococo.content.dto.CustomUserDetails;
import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.content.service.ContentService;
import com.springboot.lococo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer")
public class ContentsController {

    private final ContentService contentService;



    @PostMapping("/content")
    public ResponseEntity<ContentResponseDto> createContent(@AuthenticationPrincipal CustomUserDetails user,
                                                            @RequestPart("dto") ContentCreateDto contentCreateDto,
                                                            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        ContentEntity newContent = contentService.addContent(user.getId(), contentCreateDto, imageFile);
        ContentResponseDto responseDto = new ContentResponseDto(newContent);


        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    @PutMapping("/content/{contentId}")
    public ResponseEntity<ContentResponseDto> updateContent(@AuthenticationPrincipal CustomUserDetails user,
                                                            @PathVariable Long contentId,
                                                            @RequestPart("dto") ContentUpdateDto contentUpdateDto,
                                                            @RequestPart(value = "imageFile", required = false) MultipartFile newImageFile) throws IOException {
        ContentEntity updateContent = contentService.updateContent(user.getId(), contentId, contentUpdateDto, newImageFile);
        ContentResponseDto responseDto = new ContentResponseDto(updateContent);

        // ✅ 200 OK 상태와 함께 수정된 데이터를 응답
        return ResponseEntity.ok(responseDto);

    }



    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal CustomUserDetails user,
                                           @PathVariable Long contentId) {

        contentService.deleteContent(user.getId(), contentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/content")
    public ResponseEntity<List<ContentResponseDto>> getAllContents() {
        List<ContentEntity> contents = contentService.findAll();
        List<ContentResponseDto> responseDtos = contents.stream()
                .map(ContentResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/myContent")
    public ResponseEntity<List<ContentResponseDto>> getMyContents(@AuthenticationPrincipal CustomUserDetails user) {

        Long currentUserId = user.getId();

        // 1. 서비스에서 Entity 리스트를 가져옴
        List<ContentEntity> contents = contentService.getMyContents(currentUserId);

        // 2. Entity를 DTO로 변환하여 엔티티 노출 방지
        List<ContentResponseDto> responseDtos = contents.stream()
                .map(ContentResponseDto::new)
                .collect(Collectors.toList());

        // 3. ResponseEntity로 감싸서 반환 (API 일관성 유지)
        return ResponseEntity.ok(responseDtos);
    }

}
