package com.springboot.login_back.controller;

import com.springboot.login_back.dto.LoginRequestDto;
import com.springboot.login_back.dto.RegisterRequestDto;
import com.springboot.login_back.jwt.JwtTokenUtil;
import com.springboot.login_back.service.LogoutService;
import com.springboot.login_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.springboot.login_back.model.User;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer")
public class UserController {

    private final UserService userService;
    private static BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LogoutService logoutService;

    @Value("${SecretKey}")
    private String secretKey;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDto registerRequestDto) {
        // loginId 중복 체크
        if(userService.checkLoginIdDuplicate(registerRequestDto.getIdentification())) {
            return "로그인 아이디가 중복됩니다.";
        }

        // password와 passwordCheck가 같은지 체크
        if(!registerRequestDto.getPassword().equals(registerRequestDto.getPasswordCheck())) {
            return "비밀번호가 일치하지 않습니다.";
        }

        userService.join(registerRequestDto);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.login(loginRequestDto);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
//        if(user == null) {
//            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
//        }

        // 로그인 성공 => Jwt Token 발급


        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getIdentification(), secretKey, expireTimeMs);

        Map<String, Object> body = new HashMap<>();
        body.put("token", jwtToken);
        body.put("user", user);
        //body.put("email", user.getEmail());

        return ResponseEntity.ok(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("토큰이 유효하지 않거나 누락되었습니다.");
        }

        // "Bearer " 부분을 제외하고 실제 토큰 문자열만 추출
        String token = authorizationHeader.substring(7);

        // LogoutService를 호출하여 토큰을 블랙리스트에 추가
        logoutService.logout(token);

        return ResponseEntity.ok("로그아웃 성공");
    }
}