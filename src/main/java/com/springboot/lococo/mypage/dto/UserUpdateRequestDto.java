package com.springboot.lococo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String passwordConfirm;
    private String location;
}
