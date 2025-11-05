package com.springboot.lococo.promotion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.content.repository.ContentRepository;
import com.springboot.lococo.organizermypage.model.Proposal;
import com.springboot.lococo.organizermypage.repository.ProposalRepository;
import com.springboot.lococo.promotion.dto.BlogPostResponseDto;
import com.springboot.lococo.promotion.dto.BlogPostUpdateDto;
import com.springboot.lococo.promotion.dto.InstagramPostResponseDto;
import com.springboot.lococo.promotion.dto.InstagramPostUpdateDto;
import com.springboot.lococo.promotion.dto.PromptBuilder;
import com.springboot.lococo.promotion.model.BlogPostEntity;
import com.springboot.lococo.promotion.model.InstagramPostEntity;
import com.springboot.lococo.promotion.repository.BlogPostRepository;
import com.springboot.lococo.promotion.repository.InstagramPostRepository;
import com.vdurmont.emoji.EmojiParser; // EmojiParser import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromptService promptService;
    private final AiGenerateService aiGenerateService;
    private final ContentRepository contentRepository;
    private final InstagramPostRepository instagramPostRepository;
    private final BlogPostRepository blogPostRepository;
    private final ObjectMapper objectMapper;
    private final ProposalRepository proposalRepository;

    /**
     * 단일 인스타그램 게시물을 생성하고 저장합니다.
     * AI 응답에서 이모지를 제거한 후 DB에 저장하며, 저장된 게시물 정보를 반환합니다.
     *
     * @param contentId 특정 콘텐츠에 연결할 ID
     * @param additionalText AI 프롬프트에 추가할 텍스트
     * @return 생성 및 저장된 인스타그램 게시물의 응답 DTO
     * @throws RuntimeException AI 응답 파싱 실패 시 발생
     */
    @Transactional
    public InstagramPostResponseDto generateAndSaveInstagramPost(Long contentId, String additionalText) {
        // ContentEntity 조회 (유효성 검사)
        Proposal proposal = proposalRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + contentId));

        // AI 프롬프트 생성을 위한 데이터 빌드
        PromptBuilder promptData = PromptBuilder.builder()
                .summary(proposal.getSummary())
                .build();

        // AI에게 보낼 프롬프트 생성 및 응답 받기
        String prompt = promptService.generateInstagramPrompt(promptData, additionalText);
        String generatedContentJson = aiGenerateService.generateContent(prompt);

        try {
            // AI 응답(JSON)을 DTO로 파싱
            InstagramPostResponseDto instaDto = objectMapper.readValue(generatedContentJson, InstagramPostResponseDto.class);

            // 제목, 내용, 해시태그에서 이모지 제거
            String title = removeEmojis(instaDto.getTitle());
            String contentStr = removeEmojis(instaDto.getContent());
            String combinedHashtags = instaDto.getHashtags().stream()
                    .map(this::removeEmojis) // List의 각 해시태그에서 이모지 제거
                    .collect(Collectors.joining(" ")); // 하나의 문자열로 결합

            // 엔티티 빌드 및 저장
            InstagramPostEntity postEntity = InstagramPostEntity.builder()
                    .proposal(proposal)
                    .title(title)
                    .content(contentStr)
                    .hashtags(combinedHashtags)
                    .build();

            // DB에 저장하고 저장된 엔티티 반환
            InstagramPostEntity savedEntity = instagramPostRepository.save(postEntity);
            return new InstagramPostResponseDto(savedEntity); // 저장된 엔티티로 DTO 생성하여 반환

        } catch (JsonProcessingException e) {
            System.err.println("AI 응답 파싱 실패 (Instagram): " + e.getMessage());
            throw new RuntimeException("Failed to parse AI response for Instagram post.", e);
        }
    }

    /**
     * 단일 블로그 게시물을 생성하고 저장합니다.
     * AI 응답에서 이모지를 제거한 후 DB에 저장하며, 저장된 게시물 정보를 반환합니다.
     *
     * @param contentId 특정 콘텐츠에 연결할 ID
     * @param additionalText AI 프롬프트에 추가할 텍스트
     * @return 생성 및 저장된 블로그 게시물의 응답 DTO
     * @throws RuntimeException AI 응답 파싱 실패 시 발생
     */
    @Transactional
    public BlogPostResponseDto generateAndSaveBlogPost(Long contentId, String additionalText) {
        // ContentEntity 조회 (유효성 검사)
        Proposal proposal = proposalRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + contentId));

        // AI 프롬프트 생성을 위한 데이터 빌드
        PromptBuilder promptData = PromptBuilder.builder()
                .summary(proposal.getSummary())
                .build();

        // AI에게 보낼 프롬프트 생성 및 응답 받기
        String prompt = promptService.generateBlogPrompt(promptData, additionalText);
        String generatedContentJson = aiGenerateService.generateContent(prompt);

        try {
            // AI 응답(JSON)을 DTO로 파싱
            BlogPostResponseDto blogDto = objectMapper.readValue(generatedContentJson, BlogPostResponseDto.class);

            // 제목, 내용, 해시태그에서 이모지 제거
            String title = removeEmojis(blogDto.getTitle());
            String contentStr = removeEmojis(blogDto.getContent());
            String combinedHashtags = blogDto.getHashtags().stream()
                    .map(this::removeEmojis) // List의 각 해시태그에서 이모지 제거
                    .collect(Collectors.joining(" ")); // 하나의 문자열로 결합

            // 엔티티 빌드 및 저장
            BlogPostEntity blogEntity = BlogPostEntity.builder()
                    .proposal(proposal)
                    .title(title)
                    .content(contentStr)
                    .hashtags(combinedHashtags)
                    .build();

            // DB에 저장하고 저장된 엔티티 반환
            BlogPostEntity savedEntity = blogPostRepository.save(blogEntity);
            return new BlogPostResponseDto(savedEntity);

        } catch (JsonProcessingException e) {
            System.err.println("AI 응답 파싱 실패 (Blog): " + e.getMessage());
            throw new RuntimeException("Failed to parse AI response for blog post.", e);
        }
    }

    /**
     * 특정 인스타그램 게시물을 ID를 통해 수정합니다.
     *
     * @param id 수정할 게시물의 ID
     * @param updateDto 업데이트할 내용을 포함하는 DTO
     * @return 수정된 InstagramPostEntity
     * @throws IllegalArgumentException 해당 ID의 게시물을 찾을 수 없을 경우 발생
     */
    @Transactional
    public InstagramPostEntity updateInstagramPost(Long id, InstagramPostUpdateDto updateDto) {
        InstagramPostEntity post = instagramPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instagram post not found with ID: " + id));

        // DTO의 필드를 엔티티에 반영
        post.setTitle(updateDto.getTitle());
        post.setContent(updateDto.getContent());
        post.setHashtags(updateDto.getHashtags()); // String 형태여야 함

        return instagramPostRepository.save(post); // 변경된 엔티티 저장
    }

    /**
     * 특정 블로그 게시물을 ID를 통해 수정합니다.
     *
     * @param id 수정할 게시물의 ID
     * @param updateDto 업데이트할 내용을 포함하는 DTO
     * @return 수정된 BlogPostEntity
     * @throws IllegalArgumentException 해당 ID의 게시물을 찾을 수 없을 경우 발생
     */
    @Transactional
    public BlogPostEntity updateBlogPost(Long id, BlogPostUpdateDto updateDto) {
        BlogPostEntity blog = blogPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found with ID: " + id));

        // DTO의 필드를 엔티티에 반영
        blog.setTitle(updateDto.getTitle());
        blog.setContent(updateDto.getContent());
        blog.setHashtags(updateDto.getHashtags()); // String 형태여야 함

        return blogPostRepository.save(blog); // 변경된 엔티티 저장
    }

    /**
     * 특정 contentId에 해당하는 모든 인스타그램 게시물을 조회합니다.
     *
     * @param contentId 조회할 contentId
     * @return 해당 contentId에 속한 인스타그램 게시물 리스트
     */
    public List<InstagramPostEntity> getInstagramPostsByContentId(Long contentId) {
        return instagramPostRepository.findByProposalId(contentId);
    }

    /**
     * 특정 contentId에 해당하는 모든 블로그 게시물을 조회합니다.
     *
     * @param contentId 조회할 contentId
     * @return 해당 contentId에 속한 블로그 게시물 리스트
     */
    public List<BlogPostEntity> getBlogPostsByContentId(Long contentId) {
        return blogPostRepository.findByProposalId(contentId);
    }

    /**
     * 특정 ID의 인스타그램 게시물을 삭제합니다.
     *
     * @param id 삭제할 인스타그램 게시물의 ID
     * @throws IllegalArgumentException 해당 ID의 게시물을 찾을 수 없을 경우 발생
     */
    @Transactional
    public void deleteInstagramPost(Long id) {
        if (!instagramPostRepository.existsById(id)) {
            throw new IllegalArgumentException("Instagram post not found with ID: " + id);
        }
        instagramPostRepository.deleteById(id);
    }

    /**
     * 특정 ID의 블로그 게시물을 삭제합니다.
     *
     * @param id 삭제할 블로그 게시물의 ID
     * @throws IllegalArgumentException 해당 ID의 게시물을 찾을 수 없을 경우 발생
     */
    @Transactional
    public void deleteBlogPost(Long id) {
        if (!blogPostRepository.existsById(id)) {
            throw new IllegalArgumentException("Blog post not found with ID: " + id);
        }
        blogPostRepository.deleteById(id);
    }

    /**
     * 텍스트에서 모든 이모지를 제거하는 헬퍼 메서드입니다.
     *
     * @param text 이모지 제거할 원본 텍스트
     * @return 이모지가 제거된 텍스트
     */
    private String removeEmojis(String text) {
        if (text == null) {
            return null;
        }
        return EmojiParser.removeAllEmojis(text);
    }
}