package com.springboot.lococo.aiplanner.dto;


import com.springboot.lococo.organizermypage.model.ProposalSource;
import lombok.Data;

@Data
public class UpdateSavedProposalRequest {
    private String title;
    private String region;
    private String season;
    private String target;
    private String summary;              // 긴 본문/요약
    private ProposalSource source;       // 기본값 유지 시 null
}