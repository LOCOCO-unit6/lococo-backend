package com.springboot.lococo.mypage.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
    private String email;
    private String phoneNumber;
    private String affiliation;
}
