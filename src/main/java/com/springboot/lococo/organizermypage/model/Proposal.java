package com.springboot.lococo.organizermypage.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.springboot.lococo.model.User;
import com.springboot.lococo.model.Role;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Proposal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;          // 제안서 제목
    private String region;         // 지역(예: 경기도 용인시)
    private String season;         // 시즌(예: 여름)
    private String target;         // 타겟(예: 초등학생)
    @Column(length = 2000)
    private String summary;        // 요약/설명

    @Enumerated(EnumType.STRING)
    private ProposalSource source; // AI / USER

    private String affiliation;    // 소속 기준(리스트 필터링용)

    @ManyToOne(fetch = FetchType.LAZY)
    private User organizer;        // 작성자(또는 담당자)

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean deleted;
}