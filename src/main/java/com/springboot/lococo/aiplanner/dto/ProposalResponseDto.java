package com.springboot.lococo.aiplanner.dto;

import com.springboot.lococo.organizermypage.model.Proposal;
import com.springboot.lococo.organizermypage.model.ProposalSource;
import lombok.Getter;

@Getter
public class ProposalResponseDto {

    private Long id;
    private String title;
    private String region;
    private String season;
    private String target;
    private String summary;
    private String affiliation;
    private ProposalSource source;

    // 생성자에서 Proposal 엔티티를 DTO로 변환
    public ProposalResponseDto(Proposal proposal) {
        this.id = proposal.getId();
        this.title = proposal.getTitle();
        this.region = proposal.getRegion();
        this.season = proposal.getSeason();
        this.target = proposal.getTarget();
        this.summary = proposal.getSummary();
        this.affiliation = proposal.getAffiliation();
        this.source = proposal.getSource();
    }
}

