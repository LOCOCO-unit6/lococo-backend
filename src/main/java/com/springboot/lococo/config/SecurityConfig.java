package com.springboot.lococo.config;

import com.springboot.lococo.jwt.JwtTokenFilter;
import com.springboot.lococo.model.Role;
import com.springboot.lococo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    @Value("${SecretKey}")
    private String secretKey;
    private final RedisTemplate<String, Object> redisTemplate;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey,redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers("/api/v1/user/login", "/api/v1/admin/login", "/api/v1/register", "/api/v1/send-code", "/api/v1/verify-code", "/api/v1/organizer/status").permitAll()
                .requestMatchers("/api/v1/organizer/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers("/api/v1/**").authenticated()
                .and().build();
    }
}
