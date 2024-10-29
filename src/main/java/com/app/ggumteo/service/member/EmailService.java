package com.app.ggumteo.service.member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ConcurrentHashMap<String, String> emailVerificationCodes = new ConcurrentHashMap<>();

    public void sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        emailVerificationCodes.put(email, verificationCode);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(email);
            helper.setSubject("이메일 인증번호");
            helper.setText("인증번호는 " + verificationCode + "입니다.", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = emailVerificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            emailVerificationCodes.remove(email);
            return true;
        }
        return false;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
