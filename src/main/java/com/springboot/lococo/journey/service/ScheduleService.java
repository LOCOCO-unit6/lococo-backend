package com.springboot.lococo.journey.service;

import com.springboot.lococo.journey.dto.ActivityDto;
import com.springboot.lococo.journey.dto.SchedulePublicRequestDto;
import com.springboot.lococo.journey.dto.ScheduleRequestDto;
import com.springboot.lococo.journey.dto.ScheduleResponseDto;
import com.springboot.lococo.journey.model.TravelActivity;
import com.springboot.lococo.journey.model.TravelSchedule;
import com.springboot.lococo.journey.repository.TravelActivityRepository;
import com.springboot.lococo.journey.repository.TravelScheduleRepository;
import com.springboot.lococo.survey.dto.SurveyRequestDto;
import com.springboot.lococo.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final TravelScheduleRepository scheduleRepository;
    private final TravelActivityRepository activityRepository;
    private final GeminiApiClient geminiApiClient;
    private final SurveyService surveyService;

    // ===== 프롬프트 생성: 설문조사 기반 =====
    private String buildPrompt(ScheduleRequestDto dto) {
        return String.format("""
            장소: %s
            관심 키워드: %s
            연령대: %s
            동반자: %s

            하루 관광 일정을 추천해 주세요.
            출력 형식은 반드시 아래 순서를 따르세요:

            1줄: 여정 이름 (짧게, 20자 이내)
            2줄: 여정 요약 (짧은 한 문장)
            3줄 이후: 'HH:MM | 관광지' 형식의 활동 목록

            예시:
            종로 전통 문화 탐방
            경복궁과 북촌 한옥마을을 즐기는 당일 코스
            09:00 | 경복궁
            11:00 | 북촌 한옥마을
            14:00 | 종묘
            16:00 | 인사동 거리

            설명, 식사, 교통, 숙소는 포함하지 마세요.
            """,
                dto.getLocation(),
                String.join(", ", dto.getKeywords()),
                String.join(", ", dto.getAgeGroups()),
                dto.getCompanion()
        );
    }

    // ===== 설문 기반 여정 생성 =====
    public ScheduleResponseDto generateScheduleFromSurvey(Long surveyId) {
        SurveyRequestDto survey = surveyService.getSurveyDtoById(surveyId); // 설문 내용 가져오기
        ScheduleRequestDto requestDto = convertSurveyToScheduleRequest(survey); // Survey → Schedule 변환
        return generateSchedule(requestDto); // 기존 generateSchedule 재사용
    }

    // SurveyRequestDto → ScheduleRequestDto 변환
    private ScheduleRequestDto convertSurveyToScheduleRequest(SurveyRequestDto survey) {
        ScheduleRequestDto dto = new ScheduleRequestDto();
        dto.setStartDate(survey.getStartDate());
        dto.setLocation(survey.getLocation());
        dto.setKeywords(survey.getKeywords());
        dto.setAgeGroups(survey.getAgeRange() != null
                ? Collections.singletonList(survey.getAgeRange())
                : Collections.emptyList());
        dto.setCompanion((survey.getCompanions() != null && !survey.getCompanions().isEmpty())
                ? survey.getCompanions().get(0)
                : "혼자");
        return dto;
    }

    // ===== 여정 생성 메서드 =====
    public ScheduleResponseDto generateSchedule(ScheduleRequestDto requestDto) {
        String raw = geminiApiClient.generateContent(buildPrompt(requestDto));
        String[] lines = raw.split("\\r?\\n");

        String title = lines.length > 0 ? lines[0].trim() : "여정";
        String summary = lines.length > 1 ? lines[1].trim() : "";

        List<ActivityDto> activities = parseActivities(
                String.join("\n", Arrays.copyOfRange(lines, 2, lines.length))
        );

        // DB 저장
        TravelSchedule schedule = TravelSchedule.builder()
                .date(LocalDate.parse(requestDto.getStartDate()))
                .location(requestDto.getLocation())
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

    // ===== 파서: "HH:MM | 장소" 만 허용, 음식/쇼핑류 필터 =====
    private static final Pattern LINE_PATTERN =
            Pattern.compile("^\\s*([01]\\d|2[0-3]):[0-5]\\d\\s*\\|\\s*(.+?)\\s*$");

    private static final String[] BANNED = {
            "식사","점심","저녁","아침","브런치","맛집","카페","커피","디저트","베이커리","빵집","레스토랑",
            "식당","바","펍","술집","포차","분식","치킨","피자","버거","라멘","스시","파스타","고기","막걸리",
            "주점","와인","티룸","디너","런치","브렉퍼스트",
            "쇼핑","백화점","아울렛","면세점","마트","편의점",
            "호텔","호스텔","게스트하우스","숙소","체크인","체크아웃",
            "이동","환승","탑승","하차","셔틀","버스정류장","지하철역","공항","KTX","SRT"
    };

    private boolean isBanned(String place) {
        if (place == null) return true;
        String t = place.replace(" ", "").toLowerCase();
        for (String k : BANNED) {
            if (t.toLowerCase().contains(k.toLowerCase())) return true;
        }
        return false;
    }

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

            if (isBanned(place)) continue;

            String key = time + "|" + place;
            if (!dedup.add(key)) continue;

            ActivityDto dto = new ActivityDto();
            dto.setTime(time);
            dto.setPlace(place);
            out.add(dto);
        }
        return out;
    }

    // ===== 저장된 여정 목록 조회 =====
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

            ScheduleResponseDto dto = new ScheduleResponseDto(
                    schedule.getDate().toString(),
                    schedule.getLocation(),
                    schedule.getTitle(),
                    schedule.getSummary(),
                    activityDtos
            );

            responseList.add(dto);
        }

        return responseList;
    }



}
