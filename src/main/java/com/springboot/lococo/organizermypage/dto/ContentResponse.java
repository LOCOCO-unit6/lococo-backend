package com.springboot.lococo.organizermypage.dto;



import com.springboot.lococo.organizermypage.model.ContentType;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponse {
    private Long id;
    private String title;
    private ContentType type;
    private String thumbnailUrl;
    private String linkUrl;
}