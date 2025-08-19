package com.springboot.lococo.promotion.controller;

import com.springboot.lococo.content.dto.ContentResponseDto;
import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.promotion.dto.AdditionalTextDto;
import com.springboot.lococo.promotion.dto.PromotionResponseDto;
import com.springboot.lococo.promotion.dto.PromotionUpdateDto;
import com.springboot.lococo.promotion.model.PromotionEntity;
import com.springboot.lococo.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai/content")
public class PromotionController {
    private final PromotionService promotionService;

    /**
     * 특정 ContentId에 해당하는 콘텐츠를 기반으로 모든 홍보물을 생성하고 DB에 저장합니다.
     * @param additionalTextDto contentId와 additionalText를 포함하는 요청 DTO
     * @return 생성된 홍보물(PromotionEntity) 정보와 함께 200 OK 응답을 반환
     */
    @PostMapping("/basic")
    public ResponseEntity<PromotionResponseDto> generateAllPromotions(@RequestBody AdditionalTextDto additionalTextDto) {
        try {
            PromotionEntity generatedPromotion = promotionService.generateAllPromotions(
                    additionalTextDto.getContentId(),
                    additionalTextDto.getAdditionalText()
            );

            PromotionResponseDto responseDto = new PromotionResponseDto(generatedPromotion);

            // ✅ 3. 변환된 DTO를 성공 응답(200 OK)과 함께 반환
            return ResponseEntity.ok(responseDto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("홍보물 생성 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{promotionId}")
    public ResponseEntity<PromotionResponseDto> updatePromotion(@PathVariable Long promotionId,
                                                            @RequestBody PromotionUpdateDto promotionUpdateDto)throws IOException{
        PromotionEntity updatePromotion = promotionService.updatePromotion(promotionId, promotionUpdateDto);
        PromotionResponseDto responseDto = new PromotionResponseDto(updatePromotion);

        // ✅ 200 OK 상태와 함께 수정된 데이터를 응답
        return ResponseEntity.ok(responseDto);

    }

    @DeleteMapping("/{promotionId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long promotionId) {

        promotionService.deleteContent(promotionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/content")
    public ResponseEntity<List<PromotionResponseDto>> getAllContents() {
        List<PromotionEntity> contents = promotionService.findAll();
        List<PromotionResponseDto> responseDtos = contents.stream()
                .map(PromotionResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}
