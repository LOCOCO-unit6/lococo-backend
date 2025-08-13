package com.springboot.lococo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private String identification;
    private String password;

}
