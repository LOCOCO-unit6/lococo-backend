# MyPage 기능 구현

## 구현된 기능

### 1. 회원정보 수정
- **엔드포인트**: `PUT /api/v1/user/mypage/users/{userId}`
- **기능**: 사용자의 이메일, 전화번호, 소속 정보를 수정할 수 있습니다.
- **요청 DTO**: `UserUpdateRequestDto`
- **응답**: 수정된 `User` 객체

### 2. 콘텐츠 모아보기
- **엔드포인트**: `GET /api/v1/user/mypage/content`
- **기능**: 모든 콘텐츠(여행지, 맛집, 액티비티 등)를 조회할 수 있습니다.
- **응답**: `ContentResponseDto` 리스트

### 3. 현재 여정
- **엔드포인트**: `GET /api/v1/user/mypage/journeys/current`
- **기능**: 현재 진행 중인 여정을 조회할 수 있습니다.
- **응답**: `JourneyResponseDto`

### 4. 리뷰 작성
- **엔드포인트**: `POST /api/v1/user/mypage/reviews`
- **기능**: 새로운 리뷰를 작성할 수 있습니다.
- **요청 DTO**: `ReviewRequestDto`
- **응답**: 생성된 `Review` 객체

### 5. 리뷰 수정
- **엔드포인트**: `PUT /api/v1/user/mypage/reviews/{reviewId}`
- **기능**: 기존 리뷰를 수정할 수 있습니다.
- **요청 DTO**: `ReviewRequestDto`
- **응답**: 수정된 `Review` 객체

## 데이터베이스 스키마

### Content 테이블
- `id`: 콘텐츠 ID (Primary Key)
- `title`: 콘텐츠 제목
- `description`: 콘텐츠 설명
- `type`: 콘텐츠 타입 (여행지, 맛집, 액티비티 등)
- `location`: 위치
- `imageUrl`: 이미지 URL
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

### Journey 테이블
- `id`: 여정 ID (Primary Key)
- `title`: 여정 제목
- `description`: 여정 설명
- `destination`: 목적지
- `startDate`: 시작일
- `endDate`: 종료일
- `status`: 여정 상태 (PLANNING, ONGOING, COMPLETED, CANCELLED)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

### Review 테이블
- `id`: 리뷰 ID (Primary Key)
- `title`: 리뷰 제목
- `content`: 리뷰 내용
- `rating`: 평점 (1-5점)
- `targetType`: 리뷰 대상 타입
- `targetName`: 리뷰 대상 이름
- `imageUrl`: 이미지 URL
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

## 사용된 기술

- **Spring Boot**: 백엔드 프레임워크
- **Spring Data JPA**: 데이터 접근 계층
- **MariaDB**: 데이터베이스
- **Lombok**: 보일러플레이트 코드 제거
- **Spring Web**: REST API 구현

## 예외 처리

- Spring Boot 기본 예외 처리 사용
- `RuntimeException`을 통한 간단한 에러 메시지 전달
- 자동 HTTP 상태 코드 설정

## 주의사항

1. 모든 API는 `/api/v1/user/mypage` 경로 하위에 구현되어 있습니다.
2. 데이터베이스 연결 설정이 필요합니다 (application.properties).
3. JWT 인증이 필요한 경우 SecurityConfig에 해당 엔드포인트들을 추가해야 합니다.
