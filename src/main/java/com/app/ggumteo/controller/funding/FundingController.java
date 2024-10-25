package com.app.ggumteo.controller.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.service.funding.FundingService;
import jakarta.servlet.http.HttpServletRequest;
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
    public void videoMain(FundingDTO fundingDTO) {;}

    @GetMapping("video-header")
    public void videoHeader(FundingDTO fundingDTO) {;}


//    @GetMapping("video-main")
//    public void getMemberFundingPosts(Long memberId, Model model, HttpServletRequest request) {
////        log.info((String)request.getAttribute("data"));
//        model.addAttribute("memberId", fundingService.getMemberFundingPosts(memberId));
//    }
}
