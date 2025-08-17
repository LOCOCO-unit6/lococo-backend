package com.springboot.lococo.organizermypage.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private String contentTitle;
    private String body;
    private boolean deleteRequested;
}