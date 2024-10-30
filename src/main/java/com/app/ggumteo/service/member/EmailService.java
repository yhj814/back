package com.app.ggumteo.service.member;

import com.app.ggumteo.config.SecretConfigProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SecretConfigProperties secretConfigProperties;
    private final ConcurrentHashMap<String, String> emailVerificationCodes = new ConcurrentHashMap<>();

    public void sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        emailVerificationCodes.put(email, verificationCode);

        // 주입된 emailUsername과 emailPassword 값 확인 로그
        log.info("사용할 Email Username: {}", secretConfigProperties.getEmailUsername());
        log.info("사용할 Email Password: {}", secretConfigProperties.getEmailPassword());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(email);
            helper.setSubject("이메일 인증번호");
            helper.setText("인증번호는 " + verificationCode + "입니다.", true);
            helper.setFrom(secretConfigProperties.getEmailUsername());
            mailSender.send(message);
            log.info("이메일 발송 성공: {}", email);
        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", e.getMessage());
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
