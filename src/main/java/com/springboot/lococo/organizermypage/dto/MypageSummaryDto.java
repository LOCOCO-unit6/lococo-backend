package com.springboot.lococo.organizermypage.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageSummaryDto {
    private String displayName;  // 좌측 박스 "홍길동 님"
    private String affiliation;  // 소속
    // 필요 시 네비 badge 숫자 등 추가 가능
}