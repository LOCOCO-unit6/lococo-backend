package com.springboot.lococo.promotion.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PromptBuilder {
    private String summary;
}
