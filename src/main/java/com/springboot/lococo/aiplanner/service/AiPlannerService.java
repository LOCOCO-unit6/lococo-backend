package com.springboot.lococo.aiplanner.service;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.lococo.aiplanner.dto.*;
import com.springboot.lococo.aiplanner.model.ProposalDraft;
import com.springboot.lococo.aiplanner.support.AiPromptBuilder;
import com.springboot.lococo.model.User;
import com.springboot.lococo.organizermypage.model.Proposal;
import com.springboot.lococo.organizermypage.model.ProposalSource;
import com.springboot.lococo.organizermypage.repository.ProposalRepository;
import com.springboot.lococo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AiPlannerService {

    private final RedisTemplate<String, Object> redis;
    private final ObjectMapper objectMapper;
    private final GeminiClient geminiClient;

    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;

    private static final String PREFIX = "ai:planner:";
    private static final Duration TTL = Duration.ofHours(2);

    public Map<String, Object> debugSession(String sessionId) {
        return readSession(sessionId);
    }

    /** 1) 세션 생성 */
    public PlannerStartResponse startSession() {
        String sessionId = UUID.randomUUID().toString();
        // 빈 맵으로 초기화
        redis.opsForValue().set(PREFIX + sessionId, Map.of("createdAt", System.currentTimeMillis()), TTL);
        return new PlannerStartResponse(sessionId, "세션 생성 완료");
    }

    /** 2) 입력 저장 */
    public void saveInput(PlannerInputRequest req) {
        Map<String, Object> map = Map.of(
                "input", req,
                "updatedAt", System.currentTimeMillis()
        );
        redis.opsForValue().set(PREFIX + req.getSessionId(), map, TTL);
    }

    /** 3) AI 초안 생성 */
    public ProposalDraftResponse generateDraft(GenerateProposalRequest req) {
        Map raw = readSession(req.getSessionId());
        PlannerInputRequest stored = objectMapper.convertValue(raw.get("input"), PlannerInputRequest.class);
        if (stored == null) throw new IllegalArgumentException("세션 입력이 없습니다. 먼저 /planner/input 호출");


        // 요청에서 일부 덮어쓰기 허용
        if (req.getRegion() != null) stored.setRegion(req.getRegion());
        if (req.getTarget() != null) stored.setTarget(req.getTarget());

        String prompt = AiPromptBuilder.build(stored);
        String text = geminiClient.generate(prompt);

        // 텍스트를 섹션별로 단순 파싱
        ProposalDraft draft = parseToDraft(text, stored);

        // 세션에 저장
        Map<String, Object> map = new HashMap<>(raw);
        map.put("draft", draft);
        map.put("updatedAt", System.currentTimeMillis());
        redis.opsForValue().set(PREFIX + req.getSessionId(), map, TTL);

        return ProposalDraftResponse.builder()
                .sessionId(req.getSessionId())
                .title(draft.getTitle())
                .overview(draft.getOverview())
                .intentAndConcept(draft.getIntentAndConcept())
                .program(draft.getProgram())
                .sideEvents(draft.getSideEvents())
                .expectedEffects(draft.getExpectedEffects())
                .region(draft.getRegion())
                .season(draft.getSeason())
                .target(draft.getTarget())
                .build();


    }

    /** 4) 초안 기반 DB 저장 -> Proposal 생성 */
    public Long saveDraftToDb(SaveProposalRequest req) {
        Map raw = readSession(req.getSessionId());
        ProposalDraft draft = objectMapper.convertValue(raw.get("draft"), ProposalDraft.class);
        if (draft == null) throw new IllegalArgumentException("생성된 초안이 없습니다. /planner/proposals 먼저 호출");

        User organizer = userRepository.findById(req.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("organizerId 유효하지 않음"));

        String affiliation = (req.getAffiliation() != null && !req.getAffiliation().isBlank())
                ? req.getAffiliation()
                : organizer.getAffiliation();

        // 긴 본문을 summary에 저장(마크다운/텍스트)
        String joinedSummary = joinSummary(draft);

        Proposal p = Proposal.builder()
                .title(nv(draft.getTitle(), "제목 미정"))
                .region(nv(draft.getRegion(), "지역 미정"))
                .season(nv(draft.getSeason(), "시즌 미정"))
                .target(nv(draft.getTarget(), "타깃 미정"))
                .summary(joinedSummary)
                .source(ProposalSource.AI)
                .affiliation(affiliation)
                .organizer(organizer)
                .build();

        return proposalRepository.save(p).getId();
    }

    /** 5) 이미 저장된 Proposal 수정 */
    public void updateSaved(Long proposalId, UpdateSavedProposalRequest req) {
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

    // ---------- 내부 유틸 ----------

    private Map readSession(String sessionId) {
        Object val = redis.opsForValue().get(PREFIX + sessionId);
        if (val == null) throw new IllegalArgumentException("세션이 만료되었거나 존재하지 않습니다.");
        return objectMapper.convertValue(val, new TypeReference<Map<String, Object>>() {});
    }

    private String joinSummary(ProposalDraft d) {
        return """
                # %s

                ## 축제 개요
                %s

                ## 기획 의도 및 컨셉
                %s

                ## 프로그램 구성
                %s

                ## 참여 및 부대 행사
                %s

                ## 기대효과
                %s
                """.formatted(
                nv(d.getTitle(), "제목 미정"),
                nv(d.getOverview(), ""),
                nv(d.getIntentAndConcept(), ""),
                nv(d.getProgram(), ""),
                nv(d.getSideEvents(), ""),
                nv(d.getExpectedEffects(), "")
        ).trim();
    }

    private ProposalDraft parseToDraft(String text, PlannerInputRequest in) {
        ProposalDraft d = new ProposalDraft();
        // 제목: 첫 줄 or '# ' 이후
        String title = extractAfterHeader(text, "# ");
        if (title == null || title.isBlank()) {
            title = firstLine(text);
        }
        d.setTitle(clean(title));

        d.setOverview(extractBlock(text, "## 축제 개요"));
        d.setIntentAndConcept(extractBlock(text, "## 기획 의도 및 컨셉"));
        d.setProgram(extractBlock(text, "## 프로그램 구성"));
        d.setSideEvents(extractBlock(text, "## 참여 및 부대 행사"));
        d.setExpectedEffects(extractBlock(text, "## 기대효과"));

        d.setRegion(in.getRegion());
        d.setSeason(in.getSeasons() == null || in.getSeasons().isEmpty() ? "상관없음" : String.join(", ", in.getSeasons()));
        d.setTarget(in.getTarget());
        return d;
    }

    private String extractAfterHeader(String text, String header) {
        int idx = text.indexOf(header);
        if (idx < 0) return null;
        String sub = text.substring(idx + header.length());
        int nl = sub.indexOf('\n');
        return nl > -1 ? sub.substring(0, nl) : sub;
    }

    private String firstLine(String t) {
        int nl = t.indexOf('\n');
        return nl > -1 ? t.substring(0, nl) : t;
    }

    private String extractBlock(String text, String sectionTitle) {
        // "## 섹션" ~ 다음 "## " 직전까지
        Pattern p = Pattern.compile("(?s)##\\s*" + Pattern.quote(sectionTitle.replace("## ", "")) + "\\s*\\n(.*?)(?=\\n##\\s|\\z)");
        Matcher m = p.matcher(text);
        if (m.find()) return clean(m.group(1).trim());
        // 섹션명이 그대로 안올 때 대비해 라이트하게 탐색
        if (text.contains(sectionTitle)) return "";
        return tryLoose(text, sectionTitle);
    }

    private String tryLoose(String text, String hint) {
        int idx = text.toLowerCase().indexOf(hint.replace("## ", "").toLowerCase());
        if (idx < 0) return "";
        String sub = text.substring(idx);
        int nextIdx = sub.indexOf("\n## ");
        return clean(nextIdx > -1 ? sub.substring(0, nextIdx) : sub);
    }

    private String nv(String s, String def) { return (s == null || s.isBlank()) ? def : s; }
    private String clean(String s) { return s == null ? "" : s.replaceAll("^[#\\s:-]+", "").trim(); }
}