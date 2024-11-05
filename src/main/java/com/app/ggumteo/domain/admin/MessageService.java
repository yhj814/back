package com.app.ggumteo.domain.admin;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;

import java.util.HashMap;


public class MessageService {
    public static void main(String[] args) {
        String api_key = "YOUR_API_KEY";        // 실제 API 키로 변경
        String api_secret = "YOUR_API_SECRET";  // 실제 API 비밀키로 변경

        // CoolSMS API 객체 생성
        Message coolsms = new Message(api_key, api_secret);

        // 필수 파라미터 설정 (수신 번호, 발신 번호, 문자 타입, 메시지 내용)
        HashMap<String, String> params = new HashMap<>();
        params.put("to", "01090837645");  // 수신자 번호
        params.put("from", "01090837645"); // 발신자 번호
        params.put("type", "SMS");
        params.put("text", "당신의 남자친구를 납치했다.. 얼른 연락 해보아라.. 010908375");
        params.put("app_version", "test app 1.2"); // 앱 이름 및 버전

        try {
            // 문자 메시지 전송
            JSONObject response = (JSONObject) coolsms.send(params);
            System.out.println(response.toString());  // 성공 시 응답 출력
        } catch (CoolsmsException e) {
            // 예외 처리
            System.out.println("문자 발송 실패: " + e.getMessage());
            System.out.println("에러 코드: " + e.getCode());
        }
    }
}

