package com.springboot.lococo.organizer.main.dto;



import lombok.Data;
import java.util.List;

@Data
public class MainPageUpdateRequest {
    private String heroTitle;
    private String heroSubtitle;
    private String serviceGuide;
    private String trustMetrics;
    private String footerText;

    private List<FeatureCardDto> featureCards;
    private List<CurationItemDto> curationItems;
}