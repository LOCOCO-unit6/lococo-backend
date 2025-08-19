package com.springboot.lococo.promotion.service;

import com.springboot.lococo.promotion.dto.PromptBuilder;
import org.springframework.stereotype.Service;

@Service
public class PromptService {
    // 인스타그램 게시물용 프롬프트 생성
    public String generateInstagramPrompt(PromptBuilder request, String additionalText) {
        String basePrompt = String.format(
                "답변에 이모지나 이모티콘을 사용하지 마세요. 이 축제에 대한 인스타그램 게시물 문구를 만들어줘. #해시태그도 포함해줘. 축제명: %s, 장소: %s, 기간: %s ~ %s, 주최: %s, 설명: %s",
                request.getName(),
                request.getLocation(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOrganizer(),
                request.getText()
        );
        return appendAdditionalText(basePrompt, additionalText);
    }

    // 블로그 게시물용 프롬프트 생성
    public String generateBlogPrompt(PromptBuilder request, String additionalText) {
        String basePrompt = String.format(
                "답변에 이모지나 이모티콘을 사용하지 마세요. 이 축제에 대한 블로그 포스팅 원고를 작성해줘. 상세한 설명과 함께. 축제명: %s, 장소: %s, 기간: %s ~ %s, 주최: %s, 설명: %s",
                request.getName(),
                request.getLocation(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOrganizer(),
                request.getText()
        );
        return appendAdditionalText(basePrompt, additionalText);
    }

    // 포스터 디자인 아이디어 프롬프트 생성
    public String generatePosterPrompt(PromptBuilder request, String additionalText) {
        String basePrompt = String.format(
                "답변에 이모지나 이모티콘을 사용하지 마세요. 이 축제에 대한 포스터 제작 가이드를 만들어줘. 축제명: %s, 장소: %s, 기간: %s ~ %s, 주최: %s, 설명: %s",
                request.getName(),
                request.getLocation(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOrganizer(),
                request.getText()
        );
        return appendAdditionalText(basePrompt, additionalText);
    }

    private String appendAdditionalText(String basePrompt, String additionalText) {
        if (additionalText != null && !additionalText.trim().isEmpty()) {
            return basePrompt + String.format(", 필수 포함 문구: %s", additionalText);
        }
        return basePrompt;
    }
}
