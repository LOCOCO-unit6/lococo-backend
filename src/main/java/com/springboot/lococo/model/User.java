package com.springboot.lococo.model;

import com.springboot.lococo.content.model.ContentEntity;
import com.springboot.lococo.organizermypage.model.Proposal;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    //이름
    @Column(name = "name")
    private String name;

    //소속
    @Column(name = "affiliation")
    private String affiliation;

    //관리자, 이용자 구분
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String identification, String password, String email, String phoneNumber, String affiliation, Role role, String name) {
        this.identification = identification;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.affiliation = affiliation;
        this.name = name;
        this.role = role;
    }

    /*join 관계*/
    //contentEntity(주최자용 컨텐츠 등록과 일대다 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContentEntity> contentEntities = new ArrayList<>();

    //proposal(기획서 생성과 일대다 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Proposal> proposals = new ArrayList<>();

}
