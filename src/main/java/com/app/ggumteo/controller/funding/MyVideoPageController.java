package com.app.ggumteo.controller.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.service.funding.FundingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/*")
@Slf4j
public class MyVideoPageController {
    private final FundingService fundingService;

// SELECT
    @GetMapping("{memberId}")
    public MyFundingListDTO getMyFundingList(@PathVariable("memberId") Long memberId) {
        return fundingService.getMemberFundingPosts()
    }

//    public void getMemberFundingPosts(Long memberId, Model model, HttpServletRequest request) {
////        log.info((String)request.getAttribute("data"));
//        model.addAttribute("memberId", fundingService.getMemberFundingPosts(memberId));
//    }
}
