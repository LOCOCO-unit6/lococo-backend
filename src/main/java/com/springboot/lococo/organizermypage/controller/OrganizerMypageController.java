package com.springboot.lococo.organizermypage.controller;

import com.springboot.lococo.organizermypage.dto.*;
import com.springboot.lococo.organizermypage.model.ContentType;
import com.springboot.lococo.organizermypage.model.ProposalSource;
import com.springboot.lococo.model.User;
import com.springboot.lococo.repository.UserRepository;
import com.springboot.lococo.organizermypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * prefix: /api/v1/organizer/mypage
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer/mypage")
public class OrganizerMypageController {

    private final MypageService mypageService;
    private final UserRepository userRepository;

    /**
     * [마이페이지] 요약 (좌측 박스에 들어갈 사용자명/소속)
     * 예: GET /api/v1/organizer/mypage?organizerId=1
     * (토큰 기반으로 현재 유저를 알아내서 사용하도록 바꿔도 됨)
     */
    @GetMapping
    public ResponseEntity<MypageSummaryDto> mypage(@RequestParam Long organizerId) {
        return ResponseEntity.ok(mypageService.getSummary(organizerId));
    }

    /**
     * 회원 정보 수정
     * PUT /api/v1/organizer/mypage/users/{organizerId}
     */
    @PutMapping("/users/{organizerId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long organizerId,
            @RequestBody UpdateUserRequest req
    ) {
        mypageService.updateUser(organizerId, req);
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 관리 목록
     * GET /api/v1/organizer/mypage/reviews?organizerId=1
     * 로그인 계정의 '소속' 으로 필터링
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> reviews(@RequestParam Long organizerId) {
        User u = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        return ResponseEntity.ok(mypageService.getReviewsByAffiliation(u.getAffiliation()));
    }

    /**
     * 리뷰 삭제 요청
     * POST /api/v1/organizer/mypage/reviews/{reviewId}/delete-request
     */
    @PostMapping("/reviews/{reviewId}/delete-request")
    public ResponseEntity<Void> requestDelete(@PathVariable Long reviewId) {
        mypageService.requestReviewDelete(reviewId);
        return ResponseEntity.ok().build();
    }

    /**
     * 제안서 리스트 조회
     * GET /api/v1/organizer/mypage/proposals?organizerId=1&filter=ALL|AI|USER
     */
    @GetMapping("/proposals")
    public ResponseEntity<List<ProposalResponse>> proposals(
            @RequestParam Long organizerId,
            @RequestParam(name = "filter", required = false, defaultValue = "ALL") String filter
    ) {
        User u = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        ProposalSource source = switch (filter.toUpperCase()) {
            case "AI" -> ProposalSource.AI;
            case "USER" -> ProposalSource.USER;
            default -> null; // ALL
        };
        return ResponseEntity.ok(mypageService.getProposals(u.getAffiliation(), source));
    }

    /**
     * 제안서 수정
     * PUT /api/v1/organizer/mypage/proposals/{proposalId}
     */
    @PutMapping("/proposals/{proposalId}")
    public ResponseEntity<Void> updateProposal(
            @PathVariable Long proposalId,
            @RequestBody UpdateProposalRequest req
    ) {
        mypageService.updateProposal(proposalId, req);
        return ResponseEntity.ok().build();
    }

    /**
     * 제안서 삭제
     * DELETE /api/v1/organizer/mypage/proposals/{proposalId}
     */
    @DeleteMapping("/proposals/{proposalId}")
    public ResponseEntity<Void> deleteProposal(@PathVariable Long proposalId) {
        mypageService.deleteProposal(proposalId);
        return ResponseEntity.ok().build();
    }

    /**
     * 홍보 콘텐츠 리스트
     * GET /api/v1/organizer/mypage/contents?organizerId=1&category=ALL|POSTER|INSTAGRAM|BLOG
     */
    @GetMapping("/contents")
    public ResponseEntity<List<ContentResponse>> contents(
            @RequestParam Long organizerId,
            @RequestParam(name = "category", required = false, defaultValue = "ALL") String category
    ) {
        User u = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        ContentType type = switch (category.toUpperCase()) {
            case "POSTER" -> ContentType.POSTER;
            case "INSTAGRAM" -> ContentType.INSTAGRAM;
            case "BLOG" -> ContentType.BLOG;
            default -> null; // ALL
        };
        return ResponseEntity.ok(mypageService.getContents(u.getAffiliation(), type));
    }
}