package com.springboot.lococo.aiplanner.model;



import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProposalDraft {
    private String title;
    private String overview;
    private String intentAndConcept;
    private String program;
    private String sideEvents;
    private String expectedEffects;

    private String region;
    private String season;
    private String target;
}