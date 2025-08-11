package com.springboot.login_back.dto;

import com.springboot.login_back.model.Role;
import com.springboot.login_back.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDto {

    private String identification;
    private String password;
    private String email;
    private String phoneNumber;
    private String affiliation;
    private String role;
    private String passwordCheck;

    /*
    public User toEntity(){
        return User.builder()
                .identification(this.identification)
                .password(this.password)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .affiliation(this.affiliation)
                .role(Role.USER)
                .build();
    }
    */

    //비밀번호 단방향 해시
    public User toEntity(String encodedPassword) {
        return User.builder()
                .identification(this.identification)
                .password(encodedPassword)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .affiliation(this.affiliation)
                .role(Role.USER)
                .build();
    }
}