package com.springboot.lococo.organizermypage.service;



import com.springboot.lococo.model.User;
import com.springboot.lococo.organizermypage.dto.*;
import com.springboot.lococo.organizermypage.model.*;
import com.springboot.lococo.organizermypage.model.Proposal;
import com.springboot.lococo.organizermypage.model.ProposalSource;
import com.springboot.lococo.organizermypage.model.Review;
import com.springboot.lococo.organizermypage.repository.OrganizerContentRepository;
import com.springboot.lococo.organizermypage.repository.OrganizerReviewRepository;
import com.springboot.lococo.organizermypage.repository.ProposalRepository;
import com.springboot.lococo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final OrganizerReviewRepository reviewRepository;
    private final OrganizerContentRepository contentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 1) 마이페이지 요약
    public MypageSummaryDto getSummary(Long organizerId) {
        User u = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        String display = (u.getName() != null && !u.getName().isBlank()) ? u.getName() : u.getIdentification();
        return MypageSummaryDto.builder()
                .displayName(display + " 님")
                .affiliation(u.getAffiliation())
                .build();
    }

    // 2) 회원정보 수정
    public void updateUser(Long organizerId, UpdateUserRequest req) {
        User u = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            if (!req.getPassword().equals(req.getPasswordCheck())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        if (req.getPhoneNumber() != null) {
            u.setPhoneNumber(req.getPhoneNumber());
        }
        if (req.getAffiliation() != null) {
            u.setAffiliation(req.getAffiliation());
        }
        userRepository.save(u);
    }

    // 3) 리뷰 목록(소속 기준)
    public List<ReviewResponse> getReviewsByAffiliation(String affiliation) {
        return reviewRepository.findByAffiliationOrderByCreatedAtDesc(affiliation)
                .stream()
                .map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .contentTitle(r.getContentTitle())
                        .body(r.getBody())
                        .deleteRequested(r.isDeleteRequested())
                        .build())
                .collect(toList());
    }

    // 3-1) 리뷰 삭제요청 -> 관리자 알림 전송(여기선 flag만)
    public void requestReviewDelete(Long reviewId) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 없습니다."));
        r.setDeleteRequested(true);
        reviewRepository.save(r);
        // 실제 알림/메일/슬랙 전송은 이 지점에서 연동(추후 MessageService 등)
    }

    // 4) 제안서 리스트(필터: 전체/AI/USER)
    public List<ProposalResponse> getProposals(String affiliation, ProposalSource sourceNullable) {
        List<Proposal> list = (sourceNullable == null)
                ? proposalRepository.findByAffiliationAndDeletedFalseOrderByCreatedAtDesc(affiliation)
                : proposalRepository.findByAffiliationAndSourceAndDeletedFalseOrderByCreatedAtDesc(affiliation, sourceNullable);

        return list.stream().map(p -> ProposalResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .region(p.getRegion())
                        .season(p.getSeason())
                        .target(p.getTarget())
                        .summary(p.getSummary())
                        .source(p.getSource())
                        .build())
                .collect(toList());
    }

    // 4-1) 제안서 수정
    public void updateProposal(Long proposalId, UpdateProposalRequest req) {
        Proposal p = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안서가 없습니다."));
        if (req.getTitle() != null) p.setTitle(req.getTitle());
        if (req.getRegion() != null) p.setRegion(req.getRegion());
        if (req.getSeason() != null) p.setSeason(req.getSeason());
        if (req.getTarget() != null) p.setTarget(req.getTarget());
        if (req.getSummary() != null) p.setSummary(req.getSummary());
        if (req.getSource() != null) p.setSource(req.getSource());
        proposalRepository.save(p);
    }

    // 4-2) 제안서 삭제
    public void deleteProposal(Long proposalId) {
        Proposal p = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안서가 없습니다."));
        p.setDeleted(true); // 소프트 삭제
        proposalRepository.save(p);
    }

    // 5) 콘텐츠 리스트(필터: 전체/POSTER/INSTAGRAM/BLOG)
    public List<ContentResponse> getContents(String affiliation, ContentType typeNullable) {
        List<Content> list = (typeNullable == null)
                ? contentRepository.findByAffiliationOrderByCreatedAtDesc(affiliation)
                : contentRepository.findByAffiliationAndTypeOrderByCreatedAtDesc(affiliation, typeNullable);

        return list.stream().map(c -> ContentResponse.builder()
                .id(c.getId())
                .title(c.getTitle())
                .type(c.getType())
                .thumbnailUrl(c.getThumbnailUrl())
                .linkUrl(c.getLinkUrl())
                .build()).collect(toList());
    }
}