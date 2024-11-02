package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.MyBuyFundingListDTO;
import com.app.ggumteo.domain.funding.MyFundingBuyerListDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {
    private final MyPageService myPageService;

    @GetMapping("/member/video/my-page")
    public void read(Long id, Model model){
        MemberVO memberDTO = myPageService.getMember(id).orElseThrow();
        model.addAttribute("member", memberDTO);
    }
//    http://localhost:10000/member/video/my-page?id=1

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/funding/{memberId}/{page}")
    public MyFundingListDTO getMyFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination) {

        log.info("memberId={}", memberId);
        return myPageService.getMyFundingList(page, workAndFundingPagination, memberId);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/funding/{fundingPostId}/buyers/{page}")
    public MyFundingBuyerListDTO getFundingBuyerList(@PathVariable("fundingPostId") Long fundingPostId
            , @PathVariable("page") int page, SettingTablePagination settingTablePagination) {

        log.info("fundingPostId={}", fundingPostId);
        return myPageService.getMyFundingBuyerList(page, settingTablePagination, fundingPostId);
    }

    // UPDATE
    @ResponseBody
    @PutMapping("/members/video/my/funding/buyers/sendStatus/update")
    public void updateFundingSendStatus(@RequestBody BuyFundingProductDTO buyFundingProductDTO) {
        myPageService.updateFundingSendStatus(buyFundingProductDTO.toVO());
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/buy/funding/{memberId}/{page}")
    public MyBuyFundingListDTO getMyBuyFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination) {

        return myPageService.getMyBuyFundingList(page, workAndFundingPagination, memberId);
    }
}
