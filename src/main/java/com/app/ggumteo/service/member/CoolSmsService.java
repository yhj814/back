package com.app.ggumteo.service.member;

import com.app.ggumteo.config.CoolSmsProperties;
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

    private final String apiKey;
    private final String apiSecret;
    private final String fromPhoneNumber;

    // 인증번호와 관련된 데이터를 저장하는 Map (phoneNumber와 인증 데이터 매핑)
    private final ConcurrentHashMap<String, VerificationData> verificationDataMap = new ConcurrentHashMap<>();

    // 인증번호 만료 시간 (3분)
    private static final long EXPIRATION_TIME = 3 * 60 * 1000;

    public CoolSmsService(CoolSmsProperties coolSmsProperties) {
        this.apiKey = coolSmsProperties.getKey();
        this.apiSecret = coolSmsProperties.getSecret();
        this.fromPhoneNumber = coolSmsProperties.getNumber();

        // Logging for debugging
        log.info("API Key: {}", apiKey);
        log.info("API Secret: {}", apiSecret);
        log.info("From Phone Number: {}", fromPhoneNumber);
    }

    // SMS 발송 및 인증번호 생성 메서드
    public String sendSms(String to) throws CoolsmsException {
        VerificationData existingData = verificationDataMap.get(to);
        if (existingData != null && (System.currentTimeMillis() - existingData.getGeneratedTime()) < EXPIRATION_TIME) {
            throw new CoolsmsException("인증번호 요청이 너무 빈번합니다. 잠시 후 다시 시도해주세요.", 429);
        }

        String verificationCode = generateRandomNumber(); // 랜덤 4자리 생성
        verificationDataMap.put(to, new VerificationData(verificationCode, System.currentTimeMillis())); // 인증번호 저장

        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", fromPhoneNumber);
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + verificationCode + "] 입니다.");

        try {
            coolsms.send(params); // 메시지 전송
            log.info("SMS 발송 성공: {}", params);
        } catch (CoolsmsException e) {
            log.error("SMS 발송 실패: {}", e.getMessage());
            throw e; // 예외 재발생으로 컨트롤러에 전달
        }

        return verificationCode; // 생성된 인증번호 반환
    }


    // 인증번호 확인 메서드
    public boolean verifyCode(String phoneNumber, String code) {
        VerificationData data = verificationDataMap.get(phoneNumber);

        // 인증번호 유효성 검사 및 만료 시간 확인
        if (data != null && data.getCode().equals(code)) {
            if (System.currentTimeMillis() - data.getGeneratedTime() < EXPIRATION_TIME) {
                verificationDataMap.remove(phoneNumber); // 인증 후 코드 삭제
                return true;
            } else {
                verificationDataMap.remove(phoneNumber); // 만료된 코드 삭제
                throw new IllegalStateException("인증번호가 만료되었습니다. 다시 시도해주세요.");
            }
        }
        return false;
    }

    // 랜덤한 4자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }

    // 인증 데이터 저장용 내부 클래스
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
