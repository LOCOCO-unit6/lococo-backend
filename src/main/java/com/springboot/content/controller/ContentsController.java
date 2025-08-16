package com.springboot.content.controller;

import com.springboot.content.dto.ContentCreateDto;
import com.springboot.content.dto.ContentResponseDto;
import com.springboot.content.dto.ContentUpdateDto;
import com.springboot.content.model.ContentEntity;
import com.springboot.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ContentResponseDto> createContent(@RequestPart("dto") ContentCreateDto contentCreateDto,
                                                            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        ContentEntity newContent = contentService.addContent(contentCreateDto, imageFile);
        ContentResponseDto responseDto = new ContentResponseDto(newContent);


        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    @PutMapping("/content/{contentId}")
    public ResponseEntity<ContentResponseDto> updateContent(@PathVariable Long contentId,
                                                            @RequestPart("dto") ContentUpdateDto contentUpdateDto,
                                                            @RequestPart(value = "imageFile", required = false) MultipartFile newImageFile) throws IOException {
        ContentEntity updateContent = contentService.updateContent(contentId, contentUpdateDto, newImageFile);
        ContentResponseDto responseDto = new ContentResponseDto(updateContent);

        // ✅ 200 OK 상태와 함께 수정된 데이터를 응답
        return ResponseEntity.ok(responseDto);

    }



    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long contentId) {

        contentService.deleteContent(contentId);
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

}
