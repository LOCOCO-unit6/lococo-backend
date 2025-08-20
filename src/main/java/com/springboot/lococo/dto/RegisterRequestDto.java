package com.springboot.lococo.dto;

import com.springboot.lococo.model.Role;
import com.springboot.lococo.model.User;
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
    private Role role;
    private String passwordCheck;
    //소속이랑 이름 같이 씀
    private String name;
    private String affiliation;

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
                .affiliation(this.name)
                .affiliation(this.affiliation)
                .role(this.role)
                .build();
    }
}