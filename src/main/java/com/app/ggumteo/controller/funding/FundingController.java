package com.app.ggumteo.controller.funding;

import com.app.ggumteo.service.funding.FundingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/*")
@Slf4j
public class FundingController {
    private final FundingService fundingService;


    @GetMapping("video-main")
    public void getMemberFundingPosts(Long memberId, Model model) {
        model.addAttribute("fundingPostsVideo", fundingService.getMemberFundingPosts(memberId));
    }
}
