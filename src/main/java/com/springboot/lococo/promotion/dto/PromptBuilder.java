package com.springboot.lococo.promotion.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PromptBuilder {
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String organizer;
    private String title;
    private String text;
}
