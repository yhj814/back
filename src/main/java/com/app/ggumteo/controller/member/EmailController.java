package com.app.ggumteo.controller.member;

import com.app.ggumteo.service.member.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String email) {
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = emailService.verifyCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok("인증에 성공하였습니다.");
        } else {
            return ResponseEntity.status(400).body("인증번호가 일치하지 않습니다.");
        }
    }
}
