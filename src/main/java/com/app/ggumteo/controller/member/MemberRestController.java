package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.service.member.MemberService;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {
    private final MyPageService myPageService;

    @GetMapping("/member/video-main")
    public void read(Long id, Model model){
        MemberVO memberDTO = myPageService.getMember(id).orElseThrow();
        model.addAttribute("member", memberDTO);
    }
//    http://localhost:10000/member/video-main?memberEmail=test1@gmail.com

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/myFunding/{memberId}/{page}")
    public MyFundingListDTO getMyFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyPagePagination myPagePagination) {

        log.info("test 4={}", page);
        log.info("test 5={}", myPagePagination);
        log.info("test 6={}", memberId);
        return myPageService.getMyFundingList(page, myPagePagination, memberId);
    }

//    // SELECT
//    @ResponseBody
//    @GetMapping("/members/video/myFunding/{fundingPostId}")
//    public List<BuyFundingProductDTO> getFundingBuyerList(@PathVariable("fundingPostId") Long fundingPostId) {
//        return myPageService.getFundingBuyerList(fundingPostId);
//    }
}
