package com.springboot.lococo.aiplanner.controller;



import com.springboot.lococo.aiplanner.dto.*;
import com.springboot.lococo.aiplanner.service.AiPlannerService;
import com.springboot.lococo.content.dto.CustomUserDetails;
import com.springboot.lococo.organizermypage.model.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer/ai")
public class AiPlannerController {

    private final AiPlannerService service;

    /** [AI 기획 도우미] 세션 생성 */
    @PostMapping("/planner")
    public ResponseEntity<PlannerStartResponse> start() {
        return ResponseEntity.ok(service.startSession());
    }

    /** 기본 정보 입력 */
    @PostMapping("/planner/input")
    public ResponseEntity<Void> input(@RequestBody PlannerInputRequest req) {
        service.saveInput(req);
        return ResponseEntity.ok().build();
    }

    /** AI 제안서 초안 생성 */
    @PostMapping("/planner/proposals")
    public ResponseEntity<ProposalDraftResponse> generate(@RequestBody GenerateProposalRequest req) {
        return ResponseEntity.ok(service.generateDraft(req));
    }

    /** (초안 편집 후) DB에 저장 */
    @PostMapping("/planner/proposals/save")
    public ResponseEntity<Long> save(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestBody SaveProposalRequest req) {

        Long id = service.saveDraftToDb(user.getId(), req);
        return ResponseEntity.ok(id);
    }

    /** 저장된 제안서 수정 */
    @PutMapping("/proposals/{proposalId}")
    public ResponseEntity<Void> update(@PathVariable Long proposalId, @RequestBody UpdateSavedProposalRequest req) {
        service.updateSaved(proposalId, req);
        return ResponseEntity.ok().build();
    }

    /** 내 모든 제안서 조회 */
    @GetMapping("/proposals/my")
    public ResponseEntity<List<ProposalResponseDto>> getMyProposals(@AuthenticationPrincipal CustomUserDetails principal) {
        List<Proposal> proposals = service.getMyProposals(principal.getId());
        List<ProposalResponseDto> response = proposals.stream()
                .map(ProposalResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    /** 단일 제안서 조회 */
    @GetMapping("/proposals/{proposalId}")
    public ResponseEntity<ProposalResponseDto> getProposal(@PathVariable Long proposalId,
                                                           @AuthenticationPrincipal CustomUserDetails principal) {
        Proposal proposal = service.getProposalById(principal.getId(), proposalId);
        return ResponseEntity.ok(new ProposalResponseDto(proposal));
    }

    @GetMapping("/planner/debug")
    public ResponseEntity<Object> debug(@RequestParam String sessionId) {
        return ResponseEntity.ok(service.debugSession(sessionId));
    }
}