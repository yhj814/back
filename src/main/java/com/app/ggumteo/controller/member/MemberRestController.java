package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingBuyerListDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
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
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination) {

        log.info("test 4={}", page);
        log.info("test 5={}", workAndFundingPagination);
        log.info("test 6={}", memberId);
        return myPageService.getMyFundingList(page, workAndFundingPagination, memberId);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/fundingPost/{fundingPostId}/buyers/{page}")
    public MyFundingBuyerListDTO getFundingBuyerList(@PathVariable("fundingPostId") Long fundingPostId
            , @PathVariable("page") int page, SettingTablePagination settingTablePagination) {
        log.info("test 7={}", page);
        log.info("test 8={}", settingTablePagination);
        log.info("test 9={}", fundingPostId);
        return myPageService.getMyFundingBuyerList(page, settingTablePagination, fundingPostId);
    }

    // UPDATE
    @ResponseBody
    @PatchMapping("/members/video/fundingPost/buyers/sendStatus/update")
    public void update(@RequestBody BuyFundingProductDTO buyFundingProductDTO) {
        myPageService.setFundingSendStatus(buyFundingProductDTO.toVO());
    }
}
