package com.app.ggumteo.controller.member;

import com.app.ggumteo.aspect.annotation.MyWorkBuyerListLogStatus;
import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {
    private final MyPageService myPageService;
    private final PostFileService postFileService;

    @GetMapping("/member/video/my-page")
    public void read(Long id, Model model){
        MemberVO memberDTO = myPageService.getMember(id).orElseThrow();
        model.addAttribute("member", memberDTO);
    }
//    http://localhost:10000/member/video/my-page?id=15

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/work/{page}")
    public MyWorkListDTO getMyVideoWorkList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination, String postType) {

        return myPageService.getMyVideoWorkList(page, workAndFundingPagination, memberId, postType);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/work/{workPostId}/buyers/{page}")
    public MyWorkBuyerListDTO getMyVideoWorkBuyerList(@PathVariable("workPostId") Long workPostId
            , @PathVariable("page") int page, SettingTablePagination settingTablePagination) {

        log.info("workPostId={}", workPostId);
        return myPageService.getMyVideoWorkBuyerList(page, settingTablePagination, workPostId);
    }

    // UPDATE
    @ResponseBody
    @PutMapping("/members/video/my/work/buyers/sendStatus/update")
    public void updateWorkSendStatus(@RequestBody BuyWorkDTO buyWorkDTO) {
        myPageService.updateWorkSendStatus(buyWorkDTO.toVO());
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/funding/{page}")
    public MyFundingListDTO getMyVideoFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination, String postType) {

        log.info("memberId={}", memberId);
        return myPageService.getMyVideoFundingList(page, workAndFundingPagination, memberId, postType);
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
    @GetMapping("/members/{memberId}/video/my/buy/funding/{page}")
    public MyBuyFundingListDTO getMyBuyFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination, String postType) {

        return myPageService.getMyBuyFundingList(page, workAndFundingPagination, memberId, postType);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/inquiry/{page}")
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, WorkAndFundingPagination workAndFundingPagination) {

        return myPageService.getMyInquiryHistoryList(page, workAndFundingPagination, memberId);
    }

    // SELECT
    @ResponseBody
    @GetMapping("/members/inquiry/{inquiryId}/admin-answer")
    public Optional<AdminAnswerDTO> getAdminAnswerByInquiryId(@PathVariable("inquiryId") Long inquiryId) {
        return myPageService.getAdminAnswerByInquiryId(inquiryId);
    }


    //    가져오기
    @GetMapping("/member/video/my/work/display")
    @ResponseBody
    public byte[] display(@RequestParam("fileName") String fileName) throws IOException {
        File file = new File("C:/upload", fileName);

        log.info("fileName???????????={}", fileName);
        log.info("file???????????={}", file);

        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }
//
//        log.info("FileCopyUtils.copyToByteArray(file)???????????={}", FileCopyUtils.copyToByteArray(file));

        return FileCopyUtils.copyToByteArray(file);
    }
}
