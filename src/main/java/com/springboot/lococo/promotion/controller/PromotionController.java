package com.springboot.lococo.promotion.controller;

import com.springboot.lococo.promotion.dto.*;
import com.springboot.lococo.promotion.model.BlogPostEntity;
import com.springboot.lococo.promotion.model.InstagramPostEntity;
import com.springboot.lococo.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai/content")
public class PromotionController {

    private final PromotionService promotionService;

    // 단일 인스타그램 게시물 생성 및 저장
    @PostMapping("/instagram")
    public ResponseEntity<InstagramPostResponseDto> generateInstagramPost(@RequestBody InstagramPostRequestDto requestDto) {
        try {
            InstagramPostResponseDto responseDto = promotionService.generateAndSaveInstagramPost(
                    requestDto.getProposalId(),
                    requestDto.getAdditionalText()
            );
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 단일 블로그 게시물 생성 및 저장
    @PostMapping("/blog")
    public ResponseEntity<BlogPostResponseDto> generateBlogPost(@RequestBody BlogPostRequestDto requestDto) {
        try {
            BlogPostResponseDto responseDto = promotionService.generateAndSaveBlogPost(
                    requestDto.getProposalId(),
                    requestDto.getAdditionalText()
            );
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 특정 contentId에 해당하는 모든 게시물 조회
    @GetMapping("/instagram/{contentId}")
    public ResponseEntity<List<InstagramPostResponseDto>> getAllInstagramPosts(@PathVariable Long contentId) {
        List<InstagramPostResponseDto> responseDtos = promotionService.getInstagramPostsByContentId(contentId)
                .stream()
                .map(InstagramPostResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // 블로그 게시물 전체 조회 (분리된 엔드포인트)
    @GetMapping("/blog/{contentId}")
    public ResponseEntity<List<BlogPostResponseDto>> getAllBlogPosts(@PathVariable Long contentId) {
        List<BlogPostResponseDto> responseDtos = promotionService.getBlogPostsByContentId(contentId)
                .stream()
                .map(BlogPostResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // 인스타그램 게시물 수정 (URL에 id 포함)
    @PutMapping("/instagram/{id}")
    public ResponseEntity<InstagramPostResponseDto> updateInstagramPost(@PathVariable Long id, @RequestBody InstagramPostUpdateDto updateDto) {
        try {
            InstagramPostEntity updatedPost = promotionService.updateInstagramPost(id, updateDto);
            return ResponseEntity.ok(new InstagramPostResponseDto(updatedPost));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 블로그 게시물 수정 (URL에 id 포함)
    @PutMapping("/blog/{id}")
    public ResponseEntity<BlogPostResponseDto> updateBlogPost(@PathVariable Long id, @RequestBody BlogPostUpdateDto updateDto) {
        try {
            BlogPostEntity updatedPost = promotionService.updateBlogPost(id, updateDto);
            return ResponseEntity.ok(new BlogPostResponseDto(updatedPost));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 인스타그램 게시물 개별 삭제
    @DeleteMapping("/instagram/{id}")
    public ResponseEntity<Void> deleteInstagramPost(@PathVariable Long id) {
        try {
            promotionService.deleteInstagramPost(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 블로그 게시물 개별 삭제
    @DeleteMapping("/blog/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        try {
            promotionService.deleteBlogPost(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}