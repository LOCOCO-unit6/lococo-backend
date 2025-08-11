package com.springboot.login_back.config;

import com.springboot.login_back.jwt.JwtTokenFilter;
import com.springboot.login_back.model.Role;
import com.springboot.login_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

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
                .requestMatchers("/api/v1").authenticated()
                .requestMatchers("/api/v1/admin/**").hasAuthority(Role.ADMIN.name())
                .and().build();
    }
}
