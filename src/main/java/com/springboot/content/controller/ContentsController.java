package com.springboot.content.controller;

import com.springboot.content.dto.ContentCreateDto;
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
    public ResponseEntity<String> createContent(@RequestBody ContentCreateDto contentCreateDto) {
        contentService.addText(contentCreateDto);
        return ResponseEntity.ok("컨텐츠 생성 성공");

    }

    @PutMapping("/content/{contentId}")
    public ResponseEntity<String> updateContent(@PathVariable Long contentId, @RequestBody ContentUpdateDto contentUpdateDto) {
        contentService.updateContent(contentId, contentUpdateDto);
        return ResponseEntity.ok("컨텐츠 수정 성공");

    }



    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long contentId) {

        contentService.deleteText(contentId);
        return ResponseEntity.noContent().build();
    }

}
