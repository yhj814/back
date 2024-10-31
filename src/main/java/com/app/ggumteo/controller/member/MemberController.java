package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.member.MemberDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        boolean isLoggedIn = session.getAttribute("member") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            log.info("사용자가 로그인한 상태입니다.");
        } else {
            log.info("사용자가 로그인하지 않은 상태입니다.");
        }

        return "main";  // main.html로 이동
    }

}
