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
            // 자바로 브라우저 열기
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 데이터를 전송할때
            BufferedWriter bufferedWriter = null;
            // 객체로 사용 : 문자열로 연결하는 것 보다 객체를 사용해서 연결하는 것이 더 라소스 사용에 유리하다
            StringBuilder stringBuilder = new StringBuilder();

            connection.setRequestMethod("POST");

            // 출력을 모두보기위해 빌드 : true 디버그 : false
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


            bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            // 연결해 놓은 것을 나열
            bufferedWriter.write(stringBuilder.toString());
            // flush : 안에있는 것을 비우기 위해씀
            bufferedWriter.flush();

            // connection 성공
            if(connection.getResponseCode() == 200){
                // 응답데이터 읽기
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // 한줄한줄 가져오기
                String line = null;
                String result = "";

                // 다 가져올때까지 반복
                while((line = bufferedReader.readLine()) != null){
                    // 문자열로 연결
                    result += line;
                }
                // JSON객체를 JAVA 객체로 변형
                JsonElement jsonElement = JsonParser.parseString(result);
                // 토큰을 전달 받고 문자열로 바꿈
                accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();

                // 열은것을 닫기
                bufferedReader.close();
                bufferedWriter.close();
            }
            //input output Exception
        } catch (IOException e){
            e.printStackTrace();
        }

        return accessToken;
    }

    // 토큰으로 정보 받아오기
    public Optional<MemberDTO> getKakaoInfo(String token){
        // 카카오로그인 -> RestApi -> 사용자 정보 가져오기
        String requestURL = "https://kapi.kakao.com/v2/user/me";
        MemberDTO memberDTO = null;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            // true 해야 JSON 데이터가 넘어옴
            connection.setDoOutput(true);
            // 요청 : 액세스 토큰 방식 ("키값","value값" + 받은 토큰)
            connection.setRequestProperty("Authorization", "Bearer " + token);

            // 성공
            if(connection.getResponseCode() == 200){
                // 응답 정보 받아오기
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                String result = "";

                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                JsonElement jsonElement = JsonParser.parseString(result);

                // 카카오 정보중에 계정값 kakao_account 을 받아옴 받을때 객체로 받아야하기 때문에 getAsJsonObject
                JsonElement kakaoAccount = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject();

                // kakaoAccount객체 안에 profile 을 받아옴 받을때 객체로 받아야하기 때문에 getAsJsonObject
                JsonElement profile = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject();

                // DB에 저장
                memberDTO = new MemberDTO();
                // profile,kakaoAccount 에 각각 있는 정보 가져오기
                memberDTO.setMemberName(profile.getAsJsonObject().get("nickname").getAsString());
                memberDTO.setMemberEmail(kakaoAccount.getAsJsonObject().get("email").getAsString());
                memberDTO.setProfileImgUrl(profile.getAsJsonObject().get("profile_image_url").getAsString());
                //로그인 타입 카카오로 설정 일반은 NORMAL
//                memberDTO.setMemberLoginType(MemberLoginType.KAKAO.name());
                bufferedReader.close();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        // of :  NULL 일 수가 없을때
        // ofNullable : NUll 일 수도 있을때
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
