package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.service.member.KakaoService;
import com.app.ggumteo.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final MemberService memberService;

    @GetMapping("/kakao/login")
    public RedirectView login(String code, HttpSession session, @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        String token = kakaoService.getKakaoAccessToken(code);
        Optional<MemberDTO> kakaoInfo = kakaoService.getKakaoInfo(token);

        if (kakaoInfo.isPresent()) {
            Optional<MemberVO> existingMember = memberService.getKakaoMember(kakaoInfo.get().getMemberEmail());

            if (existingMember.isEmpty()) {
                MemberVO newMember = kakaoInfo.get().toVO();
                newMember = memberService.join(newMember);  // join 후 id가 포함된 newMember를 반환받음
                session.setAttribute("member", newMember);
                session.setAttribute("kakaoToken", token);
                log.info("최초 로그인: {}", newMember);
                log.info("세션 ID (로그인 후): {}", session.getId()); // 세션 ID 로그 추가
                return new RedirectView("/sign-up");
            } else {
                memberService.updateProfileImgUrl(kakaoInfo.get().getMemberEmail(), kakaoInfo.get().getProfileImgUrl());
                session.setAttribute("member", existingMember.get());
                session.setAttribute("kakaoToken", token);
                log.info("로그인 성공 및 프로필 이미지 업데이트: {}", existingMember.get());
                log.info("세션 ID (로그인 후): {}", session.getId()); // 세션 ID 로그 추가
                return new RedirectView(redirectUrl != null ? redirectUrl : "/main");
            }
        } else {
            log.error("카카오 정보가 존재하지 않습니다.");
            return new RedirectView("/error");
        }
    }

    @PostMapping("/kakao/logout")
    public RedirectView kakaoLogout(HttpSession session) {
        String token = (String) session.getAttribute("kakaoToken");

        if (token != null) {
            boolean isLoggedOut = kakaoService.kakaoLogout(token);

            if (isLoggedOut) {
                session.invalidate();
                log.info("로그아웃 성공");
            } else {
                log.error("로그아웃 실패");
            }
        } else {
            log.error("세션에 저장된 토큰이 없습니다.");
        }

        // 로그아웃 후 메인 페이지로 리디렉션
        return new RedirectView("/main");
    }
}
