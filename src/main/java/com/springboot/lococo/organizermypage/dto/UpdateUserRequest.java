package com.springboot.lococo.organizermypage.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserRequest {
    // 아이디/이메일은 수정불가 -> 서버에서 무시
    private String password;
    private String passwordCheck;
    private String phoneNumber;
    private String affiliation;
}