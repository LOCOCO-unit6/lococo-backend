package com.springboot.lococo.journey.service;

import com.springboot.lococo.journey.dto.ActivityDto;
import com.springboot.lococo.journey.dto.SchedulePublicRequestDto;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.model.TravelActivity;
import com.springboot.lococo.journey.model.TravelSchedule;
import com.springboot.lococo.journey.repository.TravelActivityRepository;
import com.springboot.lococo.journey.repository.TravelScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PublicJourneyService {

    private final TravelScheduleRepository scheduleRepository;
    private final TravelActivityRepository activityRepository;
    private final GeminiApiClient geminiApiClient; // GPT 호출 클라이언트

    // ===== 여정 생성 =====
    public ScheduleResponseDto generateJourneyForNonMember(SchedulePublicRequestDto dto) {

        String prompt = buildPrompt(dto);
        String raw = geminiApiClient.generateContent(prompt);

        // 줄바꿈 처리 + 빈 줄 제거
        List<String> lines = Arrays.stream(raw.split("\\r?\\n"))
                .filter(l -> !l.isBlank())
                .toList();

        String title = lines.size() > 0 ? lines.get(0).trim() : "여정";
        String summary = lines.size() > 1 ? lines.get(1).trim() : "즐거운 여행을 계획해 보세요.";

        List<ActivityDto> activities = parseActivities(
                String.join("\n", lines.subList(2, lines.size()))
        );

        TravelSchedule schedule = TravelSchedule.builder()
                .date(LocalDate.parse(dto.getStartDate()))
                .location(dto.getLocation())
                .title(title)
                .summary(summary)
                .build();
        scheduleRepository.save(schedule);

        for (ActivityDto a : activities) {
            activityRepository.save(
                    TravelActivity.builder()
                            .schedule(schedule)
                            .time(a.getTime())
                            .place(a.getPlace())
                            .build()
            );
        }

        return new ScheduleResponseDto(
                schedule.getDate().toString(),
                schedule.getLocation(),
                schedule.getTitle(),
                schedule.getSummary(),
                activities
        );
    }

    // ===== 여정 목록 조회 =====
    public List<ScheduleResponseDto> getScheduleList() {
        List<TravelSchedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponseDto> responseList = new ArrayList<>();

        for (TravelSchedule schedule : schedules) {
            List<TravelActivity> activities = activityRepository.findBySchedule(schedule);

            List<ActivityDto> activityDtos = new ArrayList<>();
            for (TravelActivity a : activities) {
                ActivityDto dto = new ActivityDto();
                dto.setTime(a.getTime());
                dto.setPlace(a.getPlace());
                activityDtos.add(dto);
            }

            responseList.add(new ScheduleResponseDto(
                    schedule.getDate().toString(),
                    schedule.getLocation(),
                    schedule.getTitle(),
                    schedule.getSummary(),
                    activityDtos
            ));
        }

        return responseList;
    }

    // ===== GPT 프롬프트 생성 =====
    private String buildPrompt(SchedulePublicRequestDto dto) {
        return String.format("""
                장소: %s
                관심 키워드: %s
                연령대: %s
                동반자: %s
                시간대: %s

                하루 관광 일정을 추천해 주세요.
                출력 형식은 반드시 아래 순서를 따르세요:
                1줄: 여정 이름 (20자 이내)
                2줄: 여정 요약 (한 문장)
                3줄 이후: 'HH:MM | 관광지' 형식의 활동 목록
                """,
                dto.getLocation(),
                String.join(", ", dto.getKeywords()),
                String.join(", ", dto.getAgeGroups()),
                dto.getCompanion(),
                String.join(", ", dto.getTimeOfDay())
        );
    }

    // ===== 활동 파서 =====
    private static final Pattern LINE_PATTERN =
            Pattern.compile("^\\s*([01]?\\d|2[0-3]):[0-5]\\d?\\s*\\|\\s*(.+?)\\s*$");

    private List<ActivityDto> parseActivities(String rawText) {
        List<ActivityDto> out = new ArrayList<>();
        if (rawText == null || rawText.isBlank()) return out;

        Set<String> dedup = new HashSet<>();
        String[] lines = rawText.split("\\r?\\n");

        for (String line : lines) {
            var m = LINE_PATTERN.matcher(line);
            if (!m.matches()) continue;

            String time = m.group(1).trim();
            String place = m.group(2).trim();

            // 중복 제거
            String key = time + "|" + place;
            if (!dedup.add(key)) continue;

            ActivityDto dto = new ActivityDto();
            dto.setTime(time);
            dto.setPlace(place);
            out.add(dto);
        }
        return out;
    }
}
