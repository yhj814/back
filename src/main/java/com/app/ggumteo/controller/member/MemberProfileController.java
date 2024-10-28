package com.app.ggumteo.controller.member;

import com.app.ggumteo.service.member.MemberProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberProfileController {
    private final MemberProfileService memberProfileService;
}
