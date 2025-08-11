package com.springboot.login_back.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //id
    @Column(name = "identification", unique = true, nullable = false)
    private String identification;

    //passward
    @Column(name = "password", nullable = false)
    private String password;

    //이메일
    @Column(name = "email", nullable = false)
    private String email;

    //전화번호
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    //소속
    @Column(name = "affiliation", nullable = false)
    private String affiliation;

    //관리자, 이용자 구분
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String identification, String password, String email, String phoneNumber, String affiliation, Role role) {
        this.identification = identification;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.affiliation = affiliation;
        this.role = role;
    }

}
