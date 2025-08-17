-- USER (비밀번호는 bcrypt 해시 필요 → 임시로 평문 저장 X)
-- 이미 BCrypt가 필요하면 Dev 전용 엔드포인트를 쓰거나 방법 B를 사용하세요.

-- 제일 먼저 유저 한 명
INSERT INTO user (identification, password, email, phone_number, name, affiliation, role)
VALUES ('lococo25', '$2a$10$3bEfIYcA9XxJH9k8rD0dZezqQZ2xE4vQd2rPhmVwzq3m8k7cV0yR6', 'lococo25@naver.com',
        '010-1234-5678', '홍길동', '용인시청', 'USER');
-- 위 해시는 "password!" 예시(BCrypt). 다르면 로그인 실패합니다.

-- 제안서
INSERT INTO proposal (title, region, season, target, summary, source, affiliation, organizer_id, created_at, updated_at, deleted)
VALUES ('용인 가나다 축제', '경기도 용인시', '여름', '초등학생', 'AI가 생성한 제안서 샘플', 'AI', '용인시청', 1, NOW(), NOW(), false);

INSERT INTO proposal (title, region, season, target, summary, source, affiliation, organizer_id, created_at, updated_at, deleted)
VALUES ('용인 ABC 축제', '경기도 용인시', '여름', '초등학생', '사용자 작성 제안서 샘플', 'USER', '용인시청', 1, NOW(), NOW(), false);

-- 콘텐츠
INSERT INTO content (title, type, thumbnail_url, link_url, affiliation, created_at)
VALUES ('행궁동 골목여행', 'BLOG', 'https://picsum.photos/seed/blog1/400/240', 'https://example.com/blog/1', '용인시청', NOW());

INSERT INTO content (title, type, thumbnail_url, link_url, affiliation, created_at)
VALUES ('용인 가나다 축제', 'INSTAGRAM', 'https://picsum.photos/seed/insta1/400/240', 'https://instagram.com/p/abc123', '용인시청', NOW());

INSERT INTO content (title, type, thumbnail_url, link_url, affiliation, created_at)
VALUES ('행궁동 골목여행 포스터', 'POSTER', 'https://picsum.photos/seed/poster1/400/240', 'https://example.com/poster/1', '용인시청', NOW());

-- 리뷰
INSERT INTO review (content_title, body, affiliation, delete_requested, created_at)
VALUES ('용인 가나다 축제', '아이들과 함께 가기 좋아요!', '용인시청', false, NOW());

INSERT INTO review (content_title, body, affiliation, delete_requested, created_at)
VALUES ('용인 ABC 축제', '주차가 조금 불편했어요.', '용인시청', false, NOW());