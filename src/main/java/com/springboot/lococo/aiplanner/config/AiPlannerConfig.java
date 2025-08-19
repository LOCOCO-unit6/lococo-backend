package com.springboot.lococo.aiplanner.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AiPlannerConfig {
    @Bean
    public RestTemplate restTemplate() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(15).toMillis());
        return new RestTemplate(factory);
    }
}