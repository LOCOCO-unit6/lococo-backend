package com.springboot.lococo.survey.controller;

import com.springboot.lococo.survey.dto.SurveyRequestDto;
import com.springboot.lococo.survey.model.UserSurvey;
import com.springboot.lococo.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/ai/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public Map<String, Object> submitSurvey(@RequestBody SurveyRequestDto dto,
                                            Authentication authentication) {
        String username = authentication.getName(); // 로그인한 사용자 이름
        UserSurvey saved = surveyService.saveSurvey(dto, username);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "설문 저장 완료");
        response.put("surveyId", saved.getId());
        return response;
    }

    @GetMapping("/{id}")
    public UserSurvey getSurvey(@PathVariable Long id) {
        return surveyService.getSurvey(id);
    }
}
