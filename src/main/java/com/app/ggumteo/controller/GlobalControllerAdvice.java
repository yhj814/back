package com.app.ggumteo.controller;

import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerAdvice {

    private final MemberService memberService; // 필요한 경우 사용

    @ModelAttribute
    public void addGlobalAttributes(HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        boolean isLoggedIn = member != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            model.addAttribute("member", member);
            model.addAttribute("memberProfile", memberProfile);
            log.info("글로벌 어드바이스 - 로그인 상태: 사용자 ID: {}, 프로필 ID: {}",
                    member.getId(),
                    memberProfile != null ? memberProfile.getId() : "null");
        } else {
            log.info("글로벌 어드바이스 - 비로그인 상태입니다.");
        }
    }
}
