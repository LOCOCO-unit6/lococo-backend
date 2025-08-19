package com.springboot.lococo.promotion.service;

import com.springboot.lococo.content.dto.ContentUpdateDto;
import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.content.repository.ContentRepository;
import com.springboot.lococo.promotion.dto.PromotionUpdateDto;
import com.springboot.lococo.promotion.dto.PromptBuilder;
import com.springboot.lococo.promotion.model.PromotionEntity;
import com.springboot.lococo.promotion.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromptService promptService;
    private final AiGenerateService aiGenerateService;
    private final ContentRepository contentRepository; // DB 연동을 위해 추가
    private final PromotionRepository promotionRepository; // DB 연동을 위해 추가


    // (이전에 논의했던) S3 업로드 서비스도 필요하다면 추가
    // private final S3Service s3Service;

    // 인스타그램, 블로그, 포스터를 한 번에 생성하고 저장하는 통합 메서드
    public PromotionEntity generateAllPromotions(Long contentId, String additionalText) {

        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + contentId));

        // 2. PromptBuilder DTO 생성
        PromptBuilder promptData = PromptBuilder.builder()
                .name(content.getName())
                .location(content.getLocation())
                .startDate(content.getStartDate())
                .endDate(content.getEndDate())
                .organizer(content.getOrganizer())
                .title(content.getTitle())
                .text(content.getText())
                .build();

        // 3. PromotionEntity 객체 생성
        PromotionEntity promotionEntity = new PromotionEntity();
        promotionEntity.setContentId(content.getId());



        // 4. 각 콘텐츠 생성 및 저장
        String instagramPrompt = promptService.generateInstagramPrompt(promptData,additionalText);
        String instagramContent = aiGenerateService.generateContent(instagramPrompt);
        System.out.println("AI 생성 인스타그램 게시물: " + instagramContent);
        promotionEntity.setInstagramPost(instagramContent);

        String blogPrompt = promptService.generateBlogPrompt(promptData, additionalText);
        String blogContent = aiGenerateService.generateContent(blogPrompt);
        System.out.println("AI 생성 블로그 포스트: " + blogContent);
        promotionEntity.setBlogPost(blogContent);

        String posterPrompt = promptService.generatePosterPrompt(promptData, additionalText);
        String posterContent = aiGenerateService.generateContent(posterPrompt);
        System.out.println("AI 생성 포스터 가이드: " + posterContent);
        promotionEntity.setPoster(posterContent);

// 6. 모든 결과물을 DB에 저장하고 반환
        return promotionRepository.save(promotionEntity);
    }

    @Transactional
    public PromotionEntity updatePromotion(Long id, PromotionUpdateDto promotionUpdateDto) throws IOException {
        PromotionEntity promotionEntity = promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다."));

        promotionEntity.setBlogPost(promotionUpdateDto.getBlogPost());
        promotionEntity.setPoster(promotionUpdateDto.getPoster());
        promotionEntity.setInstagramPost(promotionUpdateDto.getInstagramPost());


        return promotionEntity;
    }

    public void deleteContent(Long id) {

        PromotionEntity promotionEntity = promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다."));

        promotionRepository.deleteById(id);
    }

    public List<PromotionEntity> findAll() {
        return promotionRepository.findAll();
    }
}
