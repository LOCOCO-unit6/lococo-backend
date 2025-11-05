
package com.springboot.lococo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;           // JWT 토큰
    private Long id;                // 사용자 ID
    private String identification;  // 로그인 ID
    private String name;            // 이름
    private String email;           // 이메일
    private String role;
    private String message;         // 응답 메시지 (예: "로그인 성공")

}
