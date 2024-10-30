package com.app.ggumteo.service.member;

import com.app.ggumteo.config.SecretConfigProperties;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CoolSmsService {

    private final SecretConfigProperties secretConfigProperties;
    private final ConcurrentHashMap<String, VerificationData> verificationDataMap = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 3 * 60 * 1000;

    public CoolSmsService(SecretConfigProperties secretConfigProperties) {
        this.secretConfigProperties = secretConfigProperties;

        // API 키와 시크릿을 로그로 확인
        log.info("CoolSMS API Key: {}", secretConfigProperties.getCoolSmsApiKey());
        log.info("CoolSMS API Secret: {}", secretConfigProperties.getCoolSmsApiSecret());
        log.info("CoolSMS Sender Number: {}", secretConfigProperties.getCoolSmsNumber());
    }

    public String sendSms(String to) throws CoolsmsException {
        VerificationData existingData = verificationDataMap.get(to);
        if (existingData != null && (System.currentTimeMillis() - existingData.getGeneratedTime()) < EXPIRATION_TIME) {
            throw new CoolsmsException("인증번호 요청이 너무 빈번합니다. 잠시 후 다시 시도해주세요.", 429);
        }

        String verificationCode = generateRandomNumber();
        verificationDataMap.put(to, new VerificationData(verificationCode, System.currentTimeMillis()));

        Message coolsms = new Message(secretConfigProperties.getCoolSmsApiKey(), secretConfigProperties.getCoolSmsApiSecret());

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", secretConfigProperties.getCoolSmsNumber());
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + verificationCode + "] 입니다.");

        try {
            coolsms.send(params);
            log.info("SMS 발송 성공: {}", params);
        } catch (CoolsmsException e) {
            log.error("SMS 발송 실패: {}", e.getMessage());
            throw e;
        }

        return verificationCode;
    }

    public boolean verifyCode(String phoneNumber, String code) {
        VerificationData data = verificationDataMap.get(phoneNumber);
        if (data != null && data.getCode().equals(code)) {
            if (System.currentTimeMillis() - data.getGeneratedTime() < EXPIRATION_TIME) {
                verificationDataMap.remove(phoneNumber);
                return true;
            } else {
                verificationDataMap.remove(phoneNumber);
                throw new IllegalStateException("인증번호가 만료되었습니다. 다시 시도해주세요.");
            }
        }
        return false;
    }

    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }

    private static class VerificationData {
        private final String code;
        private final long generatedTime;

        public VerificationData(String code, long generatedTime) {
            this.code = code;
            this.generatedTime = generatedTime;
        }

        public String getCode() {
            return code;
        }

        public long getGeneratedTime() {
            return generatedTime;
        }
    }
}
