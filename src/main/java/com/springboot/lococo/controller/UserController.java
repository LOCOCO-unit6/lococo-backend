package com.springboot.lococo.controller;

import com.springboot.lococo.dto.LoginRequestDto;
import com.springboot.lococo.dto.RegisterRequestDto;
import com.springboot.lococo.jwt.JwtTokenUtil;
import com.springboot.lococo.service.LogoutService;
import com.springboot.lococo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.springboot.lococo.model.User;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
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

    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.userLogin(loginRequestDto);

        if (user == null) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", "로그인에 실패했습니다. 계정 정보를 확인하거나 관리자에게 문의하세요.");
            return ResponseEntity.status(401).body(errorBody);
        }

        // 로그인 성공 => Jwt Token 발급


        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getIdentification(), secretKey, expireTimeMs);

        Map<String, Object> body = new HashMap<>();
        body.put("token", jwtToken);
        body.put("user", user);
        body.put("message", "로그인 성공");

        return ResponseEntity.ok(body);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.adminLogin(loginRequestDto);

        if (user == null) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", "로그인에 실패했습니다. 계정 정보를 확인하거나 관리자에게 문의하세요.");
            return ResponseEntity.status(401).body(errorBody);
        }

        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getIdentification(), secretKey, expireTimeMs);

        Map<String, Object> body = new HashMap<>();
        body.put("token", jwtToken);
        body.put("user", user);
        body.put("message", "로그인 성공");

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