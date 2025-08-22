package com.springboot.lococo.aiplanner.support;


import com.springboot.lococo.aiplanner.dto.PlannerInputRequest;

public class AiPromptBuilder {

    public static String build(PlannerInputRequest in) {
        String season = in.getSeasons() == null || in.getSeasons().isEmpty()
                ? "상관없음" : String.join(", ", in.getSeasons());

        String brief = (in.getBrief() == null || in.getBrief().isBlank()) ? "특이사항 없음" : in.getBrief();
        String local = (in.getLocalSpecialties() == null || in.getLocalSpecialties().isBlank()) ? "특산품 없음" : in.getLocalSpecialties();

        return """
                당신은 한국 로컬 축제 PM입니다. 입력 정보를 바탕으로, **한국어**로 축제 제안서 **초안**을 구조화해서 작성하세요.
                출력은 아래 섹션 제목을 그대로 사용하고, 깔끔한 문장과 불릿을 섞어 가독성 있게 작성합니다. 각 섹션은 6~12문장 내외.

                [입력]
                - 지역: %s
                - 시즌: %s
                - 타깃: %s
                - 지역 특산물/키워드: %s
                - 간단 설명: %s

                [출력 템플릿]
                # 제목
                (한 줄로 임팩트 있는 행사명)

                ## 축제 개요
                - 축제명:
                - 슬로건:
                - 일정:
                - 장소:
                - 타깃:

                ## 기획 의도 및 컨셉
                (배경, 문제의식, 핵심 컨셉)

                ## 프로그램 구성
                (메인 무대/이벤트, 체험/전시, 푸드/마켓 등 소제목 + 불릿)

                ## 참여 및 부대 행사
                (참여형 요소, 사진 포인트, 지역 상권 연계 등)

                ## 기대효과
                (유입/지역경제/브랜딩/지속가능성 등)

                """.formatted(
                nv(in.getRegion()),
                season,
                nv(in.getTarget()),
                local,
                brief
        );
    }

    private static String nv(String s) { return (s == null || s.isBlank()) ? "미정" : s; }
}