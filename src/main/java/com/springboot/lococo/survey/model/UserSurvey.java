package com.springboot.lococo.survey.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class UserSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // 로그인한 유저 이름

    @Column(nullable = false)
    private String identification; // 유저 고유 식별자

    private String startDate;
    private String endDate;
    private String location;
    private String ageRange;
    private String travelStyle;
    private int dailyBudget;
    private String note;

    @ElementCollection
    private List<String> keywords;

    @ElementCollection
    private List<String> companions;

    @ElementCollection
    private List<String> mustVisit;

    @ElementCollection
    private List<String> imageHints;
}
