package com.springboot.lococo.organizermypage.dto;


import com.springboot.lococo.organizermypage.model.ProposalSource;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponse {
    private Long id;
    private String title;
    private String region;
    private String season;
    private String target;
    private String summary;
    private ProposalSource source;
}