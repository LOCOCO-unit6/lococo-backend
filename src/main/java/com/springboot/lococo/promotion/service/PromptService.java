package com.springboot.lococo.promotion.service;

import com.springboot.lococo.promotion.dto.PromptBuilder;
import org.springframework.stereotype.Service;

@Service
public class PromptService {
    // 인스타그램 게시물용 프롬프트 생성
    public String generateInstagramPrompt(PromptBuilder request, String additionalText) {
        String basePrompt = String.format(
                "다음 축제 정보를 바탕으로 인스타그램 게시물 초안을 작성해줘. 이모지나 이모티콘은 사용하지 마세요.\n" +
                        "축제명: %s\n장소: %s\n기간: %s ~ %s\n주최: %s\n설명: %s",
                request.getName(),
                request.getLocation(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOrganizer(),
                request.getText()
        );

        String finalPrompt = appendAdditionalText(basePrompt, additionalText);

        // **핵심 수정 부분:** AI에게 최종적으로 JSON 형식을 지시
        return finalPrompt + "\n\n" +
                "위 정보를 바탕으로 아래 JSON 스키마에 맞춰 응답해줘.\n" +
                "{\"title\": \"[게시물 제목]\", \"content\": \"[게시물 내용]\", \"hashtags\": \"[#해시태그 #목록]\"}";
    }

    // 블로그 게시물용 프롬프트 생성
    public String generateBlogPrompt(PromptBuilder request, String additionalText) {
        String basePrompt = String.format(
                "다음 축제 정보를 바탕으로 블로그 포스팅 원고를 작성해줘. 이모지나 이모티콘은 사용하지 마세요.\n" +
                        "축제명: %s\n장소: %s\n기간: %s ~ %s\n주최: %s\n설명: %s",
                request.getName(),
                request.getLocation(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOrganizer(),
                request.getText()
        );

        String finalPrompt = appendAdditionalText(basePrompt, additionalText);

        // **핵심 수정 부분:** AI에게 최종적으로 JSON 형식을 지시
        return finalPrompt + "\n\n" +
                "위 정보를 바탕으로 아래 JSON 스키마에 맞춰 응답해줘.\n" +
                "{\"title\": \"[블로그 제목]\", \"content\": \"[블로그 내용]\", \"hashtags\": \"[#해시태그 #목록]\"}";
    }

    // 포스터 디자인 아이디어 프롬프트 생성
//    public String generatePosterPrompt(PromptBuilder request, String additionalText) {
//        String basePrompt = String.format(
//                "답변에 이모지나 이모티콘을 사용하지 마세요. 이 축제에 대한 포스터 제작 가이드를 만들어줘. 축제명: %s, 장소: %s, 기간: %s ~ %s, 주최: %s, 설명: %s",
//                request.getName(),
//                request.getLocation(),
//                request.getStartDate(),
//                request.getEndDate(),
//                request.getOrganizer(),
//                request.getText()
//        );
//        return appendAdditionalText(basePrompt, additionalText);
//    }

    private String appendAdditionalText(String basePrompt, String additionalText) {
        if (additionalText != null && !additionalText.trim().isEmpty()) {
            return basePrompt + String.format(", 필수 포함 문구: %s", additionalText);
        }
        return basePrompt;
    }
}
