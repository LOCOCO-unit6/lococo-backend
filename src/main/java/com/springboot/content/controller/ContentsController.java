package com.springboot.content.controller;

import com.springboot.content.dto.ContentCreateDto;
import com.springboot.content.dto.ContentResponseDto;
import com.springboot.content.dto.ContentUpdateDto;
import com.springboot.content.model.ContentEntity;
import com.springboot.content.repository.ContentRepository;
import com.springboot.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer")
public class ContentsController {

    private final ContentService contentService;



    @PostMapping("/content")
    public ResponseEntity<ContentResponseDto> createContent(@RequestBody ContentCreateDto contentCreateDto) {
        ContentEntity newContent = contentService.addText(contentCreateDto);
        ContentResponseDto responseDto = new ContentResponseDto(newContent);

        // ✅ 201 Created 상태와 함께 생성된 데이터를 응답
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    @PutMapping("/content/{contentId}")
    public ResponseEntity<ContentResponseDto> updateContent(@PathVariable Long contentId, @RequestBody ContentUpdateDto contentUpdateDto) {
        ContentEntity updatedContent = contentService.updateContent(contentId, contentUpdateDto);
        ContentResponseDto responseDto = new ContentResponseDto(updatedContent);

        // ✅ 200 OK 상태와 함께 수정된 데이터를 응답
        return ResponseEntity.ok(responseDto);

    }



    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long contentId) {

        contentService.deleteText(contentId);
        return ResponseEntity.noContent().build();
    }

}
