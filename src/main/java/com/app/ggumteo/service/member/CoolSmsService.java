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

    // 인증번호를 임시 저장하는 Map (phoneNumber와 인증번호 매핑)
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();

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
        String verificationCode = generateRandomNumber(); // 랜덤 4자리 생성
        verificationCodes.put(to, verificationCode); // 인증번호 저장

        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", fromPhoneNumber);
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + verificationCode + "] 입니다.");

        coolsms.send(params); // 메시지 전송
        return verificationCode; // 생성된 인증번호 반환
    }

    // 인증번호 확인 메서드
    public boolean verifyCode(String phoneNumber, String code) {
        String savedCode = verificationCodes.get(phoneNumber);
        if (savedCode != null && savedCode.equals(code)) {
            verificationCodes.remove(phoneNumber); // 인증 후 코드 삭제
            return true;
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
}
