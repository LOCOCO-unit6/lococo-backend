package com.springboot.lococo.controller;

import com.springboot.lococo.dto.LoginRequestDto;
import com.springboot.lococo.dto.LoginResponseDto;
import com.springboot.lococo.dto.RegisterRequestDto;
import com.springboot.lococo.jwt.JwtTokenUtil;
import com.springboot.lococo.service.LogoutService;
import com.springboot.lococo.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.springboot.lococo.model.User;





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
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto) {
        // loginId 중복 체크
        // 1. 아이디 중복 시 -> 409 Conflict (실패)
        if(userService.checkLoginIdDuplicate(registerRequestDto.getIdentification())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("로그인 아이디가 중복됩니다.");
        }

        // 2. 비밀번호 불일치 시 -> 400 Bad Request (실패)
        if(!registerRequestDto.getPassword().equals(registerRequestDto.getPasswordCheck())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호가 일치하지 않습니다.");
        }

        // 3. 진짜 성공 시 -> 201 Created (성공)
        userService.join(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("회원가입 성공");
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.userLogin(loginRequestDto);

        if (user == null) {
            // 로그인 실패 시
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(null, null, null, null, null, null, "로그인 실패"));
        }

        // 로그인 성공 시 JWT 발급
        long expireTimeMs = 1000 * 60 * 60; // 60분
        String jwtToken = JwtTokenUtil.createToken(user.getId(), secretKey, expireTimeMs);

        // DTO에 필요한 정보만 담기
        LoginResponseDto response = new LoginResponseDto(
                jwtToken,
                user.getId(),
                user.getIdentification(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                "로그인 성공"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponseDto> adminLogin(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.adminLogin(loginRequestDto);

        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body(new LoginResponseDto(null, null, null, null, null, null, "로그인 실패"));
        }

        long expireTimeMs = 1000 * 60 * 60; // 1시간
        String jwtToken = JwtTokenUtil.createToken(user.getId(), secretKey, expireTimeMs);

        // 응답 DTO에 모든 정보 포함
        LoginResponseDto dto = new LoginResponseDto(
                jwtToken,
                user.getId(),
                user.getIdentification(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                "로그인 성공"
        );

        return ResponseEntity.ok(dto);
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