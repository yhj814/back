package com.app.ggumteo.service.member;

import com.app.ggumteo.domain.member.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class KakaoService {
    // 토큰 받아오는 메소드
    public String getKakaoAccessToken(String code){
        String accessToken = null;
        // 토큰을 요청하는 경로
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 고정
            stringBuilder.append("grant_type=authorization_code");
            // 앱키 -> REST API 개인 키
            stringBuilder.append("&client_id=4759c67a8c35dab21cbdb77f0bb159ad");
            // 카카오 로그인 -> Redirect URI -> 요청 주소
            stringBuilder.append("&redirect_uri=http://localhost:10000/kakao/login");
            // getKakaoAccessToken 에서 받은 code
            stringBuilder.append("&code=" + code);
            // 제품설정 보안 -> Client Secret -> 개인코드
            stringBuilder.append("&client_secret=et1SxUxPAgjDnLShd0XEZZaOILYczIPI");

            // 로그로 요청 데이터 확인
            log.info("Kakao token request data: " + stringBuilder.toString());

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();

            // connection 성공
            int responseCode = connection.getResponseCode();
            log.info("Kakao token response code: " + responseCode);  // 응답 코드 로그 출력

            if (responseCode == 200) {
                // 응답데이터 읽기
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                log.info("Kakao token response: " + result);  // 응답 데이터 로그 출력

                // JSON객체를 JAVA 객체로 변형
                JsonElement jsonElement = JsonParser.parseString(result);
                // 토큰을 전달 받고 문자열로 바꿈
                accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();

                bufferedReader.close();
                bufferedWriter.close();

                log.info("Kakao access token: " + accessToken);  // 토큰 로그 출력
            } else {
                log.error("Failed to get access token. Response code: " + responseCode);
            }
        } catch (IOException e) {
            log.error("Error while getting access token: " + e.getMessage(), e);
        }

        return accessToken;
    }

    // 토큰으로 정보 받아오기
    public Optional<MemberDTO> getKakaoInfo(String token){
        String requestURL = "https://kapi.kakao.com/v2/user/me";
        MemberDTO memberDTO = null;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = connection.getResponseCode();
            log.info("Kakao info response code: " + responseCode);  // 응답 코드 로그 출력

            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                log.info("Kakao info response: " + result);  // 응답 데이터 로그 출력

                JsonElement jsonElement = JsonParser.parseString(result);
                JsonElement kakaoAccount = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject();
                JsonElement profile = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject();

                memberDTO = new MemberDTO();

                memberDTO.setMemberEmail(kakaoAccount.getAsJsonObject().get("email").getAsString());
                memberDTO.setProfileImgUrl(profile.getAsJsonObject().get("profile_image_url").getAsString());

                bufferedReader.close();
            } else {
                log.error("Failed to get Kakao info. Response code: " + responseCode);
            }

        } catch (IOException e) {
            log.error("Error while getting Kakao info: " + e.getMessage(), e);
        }

        return Optional.ofNullable(memberDTO);
    }

    // 카카오 로그아웃
    public boolean kakaoLogout(String token) {
        String requestURL = "https://kapi.kakao.com/v1/user/logout";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            // 로그아웃 요청
            if (connection.getResponseCode() == 200) {
                return true; // 로그아웃 성공
            } else {
                return false; // 로그아웃 실패
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 예외 발생 시 로그아웃 실패
        }
    }
}
