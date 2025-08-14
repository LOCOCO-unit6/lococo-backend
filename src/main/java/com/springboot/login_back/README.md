[이용객 파일 구조도]



src/main/java/com/springboot/login_back/user/
├── mypage/                          # 마이페이지 기능
│   ├── controller/
│   │   └── MyPageController.java
│   ├── dto/
│   │   ├── ContentResponseDto.java
│   │   ├── JourneyResponseDto.java
│   │   ├── ReviewRequestDto.java
│   │   └── UserUpdateRequestDto.java
│   ├── model/
│   │   ├── Content.java
│   │   ├── Journey.java
│   │   └── Review.java
│   ├── repository/
│   │   ├── ContentRepository.java
│   │   ├── JourneyRepository.java
│   │   └── ReviewRepository.java
│   └── service/
│       └── MyPageService.java
│
└── mainpage/                        # 메인 페이지 
    ├── controller/
    │   └── MainPageController.java
    ├── dto/
    │   ├── RecommendedCourseResponseDto.java
    │   └── ReviewResponseDto.java
    ├── model/
    │   ├── RecommendedCourse.java
    │   └── Review.java
    ├── repository/
    │   ├── RecommendedCourseRepository.java
    │   └── MainPageReviewRepository.java
    └── service/
        └── MainPageService.java