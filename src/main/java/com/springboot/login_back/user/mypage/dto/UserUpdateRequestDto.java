package com.springboot.login_back.user.mypage.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
    private String identification;
    private String password;
    private String passwordConfirm;
    private String name;
    private String location;
    private String email;
    private String phoneNumber;
}
