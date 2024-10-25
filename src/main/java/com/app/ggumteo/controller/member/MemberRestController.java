package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
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
    public void read(String memberEmail, Model model){
        MemberVO memberDTO = myPageService.getMember(memberEmail).orElseThrow();
        model.addAttribute("member", memberDTO);
    }
//    http://localhost:10000/member/video-main?memberEmail=test1@gmail.com

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}")
    public List<FundingDTO> getMyFundingList(@PathVariable("memberId") Long memberId) {
        return myPageService.getMyFundingList(memberId);
    }
}
