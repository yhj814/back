package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.service.member.KakaoService;
import com.app.ggumteo.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final MemberService memberService;

    @GetMapping("/kakao/login")
    public RedirectView login(String code, HttpSession session) {
        // 카카오 서버로부터 액세스 토큰을 받아옴
        String token = kakaoService.getKakaoAccessToken(code);
        Optional<MemberDTO> kakaoInfo = kakaoService.getKakaoInfo(token);

        if(kakaoInfo.isPresent()) {
            // 카카오 정보로 회원 가입 로직 실행 (필요 시)
            memberService.join(kakaoInfo.get().toVO());

            // 사용자 정보를 세션에 저장
            session.setAttribute("member", memberService.getKakaoMember(kakaoInfo.get().getMemberEmail()).get());

            // 액세스 토큰을 세션에 저장
            session.setAttribute("kakaoToken", token);

            log.info("로그인 성공: " + memberService.getKakaoMember(kakaoInfo.get().getMemberEmail()).get());
        } else {
            log.error("카카오 정보가 존재하지 않습니다.");
        }

        return new RedirectView("/header-login");
    }

    @PostMapping("/kakao/logout")
    public RedirectView kakaoLogout(HttpSession session) {
        // 세션에서 카카오 액세스 토큰을 가져옴
        String token = (String) session.getAttribute("kakaoToken");

        if (token != null) {
            boolean isLoggedOut = kakaoService.kakaoLogout(token);

            if (isLoggedOut) {
                session.invalidate(); // 세션 무효화 (로그아웃 처리)
                log.info("로그아웃 성공");
            } else {
                log.error("로그아웃 실패");
            }
        } else {
            log.error("세션에 저장된 토큰이 없습니다.");
        }

        // 로그아웃 후 /header-logout 경로로 리디렉션
        return new RedirectView("/header-logout");
    }
}
