package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.member.MemberDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/header-logout")
    public void goToLoginPage(MemberDTO memberDTO) {;}

    @GetMapping("header-login")
    public void goToLogoutPage(MemberDTO memberDTO) {;}

}
