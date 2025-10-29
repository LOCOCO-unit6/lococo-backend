package com.springboot.lococo.journey.model;

import com.springboot.lococo.mypage.model.UserReview;
import com.springboot.lococo.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TravelSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;        // 일정 날짜
    private String location;       // 지역 (예: "서울 종로구")

    private String title;          // 여정 이름 (예: "종로 전통 문화 탐방")
    private String summary;        // 짧은 세부정보 (예: "경복궁과 북촌 한옥마을 중심의 당일 코스")

    // 일정 생성 방식 구분
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType scheduleType; // AI_GENERATED, USER_CREATED

    // 사용자 연결 (사용자가 직접 생성한 일정의 경우)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TravelActivity> activities = new ArrayList<>();

    // 리뷰 목록
    @OneToMany(mappedBy = "travelSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserReview> reviews = new ArrayList<>();

    public enum ScheduleType {
        AI_GENERATED,    // AI가 생성한 일정
        USER_CREATED     // 사용자가 직접 생성한 일정
    }
}

