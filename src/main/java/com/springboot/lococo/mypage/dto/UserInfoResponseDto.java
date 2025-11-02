package com.springboot.lococo.mypage.dto;

import com.springboot.lococo.model.User; // User 엔티티가 있는 패키지로 변경하세요.
import com.springboot.lococo.model.Role; // Role enum이 있는 패키지로 변경하세요.
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor // 역직렬화를 위한 기본 생성자
@AllArgsConstructor // Builder를 위한 전체 생성자
public class UserInfoResponseDto {

    private Long id;
    private String identification;
    private String email;
    private String phoneNumber;
    private String affiliation;
    private Role role;


    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .identification(user.getIdentification())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .affiliation(user.getAffiliation())
                .role(user.getRole())
                .build();
    }
}
