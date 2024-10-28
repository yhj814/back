package com.app.ggumteo.controller.member;

import com.app.ggumteo.service.member.CoolSmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class CoolSmsController {

    private final CoolSmsService coolSmsService;

    // SMS 전송 엔드포인트
    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestParam String phoneNumber) {
        try {
            String verificationCode = coolSmsService.sendSms(phoneNumber);
            return ResponseEntity.ok("인증번호가 발송되었습니다.");
        } catch (CoolsmsException e) {
            return ResponseEntity.status(500).body("SMS 발송에 실패하였습니다.");
        }
    }

    // 인증번호 확인 엔드포인트
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean isVerified = coolSmsService.verifyCode(phoneNumber, code);
        if (isVerified) {
            return ResponseEntity.ok("인증에 성공하였습니다.");
        } else {
            return ResponseEntity.status(400).body("인증번호가 일치하지 않습니다.");
        }
    }
}
