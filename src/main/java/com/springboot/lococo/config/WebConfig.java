package com.springboot.lococo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",       // 로컬 개발 환경용 (PC에서 개발할 때)
                        "http://13.55.41.77:3000",     // 아마도 다른 개발/테스트 환경용?
                        "http://13.55.41.77"  )        // Nginx를 통해 접속하는 실제 배포 환경용)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
