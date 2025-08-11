package com.springboot.login_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private String identification;
    private String password;

}
