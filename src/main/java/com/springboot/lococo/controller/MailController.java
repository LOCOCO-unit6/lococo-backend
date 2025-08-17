package com.springboot.lococo.controller;

import com.springboot.lococo.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer")
public class MailController {

    private final VerificationService verificationService;

    //인증코드 전송
    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestParam String email) {
        verificationService.sendCode(email);
        return ResponseEntity.ok().build();
    }

    //코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean ok = verificationService.verifyCode(email, code);
        return ResponseEntity.ok(ok);
    }

    //인증 상태 확인 (프론트에서 폼 제출 전 체크용)
    @GetMapping("/status")
    public ResponseEntity<Boolean> status(@RequestParam String email) {
        return ResponseEntity.ok(verificationService.isVerified(email));
    }
}
