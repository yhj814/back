package com.app.ggumteo.controller.member;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.MyApplicationAuditionListDTO;
import com.app.ggumteo.domain.audition.MyAuditionApplicantListDTO;
import com.app.ggumteo.domain.audition.MyAuditionListDTO;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.exception.SessionNotFoundException;
import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MySettingTablePagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.myPage.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {
    private final MyPageService myPageService;
    private final HttpSession session;

    @ModelAttribute
    public void setMemberInfo(HttpSession session, Model model) {
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        boolean isLoggedIn = memberVO != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            model.addAttribute("member", memberVO);
            model.addAttribute("memberProfile", memberProfile);
            log.info("로그인 상태 - 사용자 ID: {}, 프로필 ID: {}", memberVO.getId(), memberProfile != null ? memberProfile.getId() : "null");
        } else {
            log.info("비로그인 상태입니다.");
        }
    }

    @GetMapping("/member/video/my-page")
    public String goToReadForm() {
        return "/member/video/my-page";
    }

//    @GetMapping("update/{id}")
//    public String updateMemberProfile(@PathVariable("id") Long id, Model model) {
//        Optional<MemberVO> memberVO = myPageService.getMember(id);
//        log.info("memberVO: {}", memberVO);  // member 객체를 로그로 출력해 확인
//
//        if (memberVO != null) {  // member - null 인지 아닌지 확인
//            model.addAttribute("member", memberVO);
//            return "/member/video/my-page";
//        } else {
//            // member - null 인 경우 처리 (예: 에러 페이지로 이동??????)
//            model.addAttribute("error", "회원 정보를 찾을 수 없습니다.");
//            return "/error/404";
//        }
//    }
//
//    @PostMapping("/member/video/my-page")
//    public RedirectView updateMemberProfile(@ModelAttribute MemberProfileDTO memberProfileDTO) {
//        try {
//            log.info("수정 요청 - 회원 프로필 정보: {}", memberProfileDTO);
//
////            // 기존 데이터를 가져와서 필요한 필드를 설정
////            WorkDTO currentWork = myPageService.findWorkById(workDTO.getId());
//
//            // 서비스에서 작품 업데이트 로직 실행
//            myPageService.updateMemberProfile(memberProfileDTO.toVO());
//            return new RedirectView("/text/detail/" + workDTO.getId());
//        } catch (Exception e) {
//            log.error("Error updating work: ", e);
//            return new RedirectView("/text/modify/" + workDTO.getId());
//        }
//    }



//    @PostMapping("delete")
//    public RedirectView softDeleteMember(@ModelAttribute MemberDTO memberDTO) {
//        MemberVO memberVO = (MemberVO) session.getAttribute("member");
//        if (memberVO == null) {
//            log.error("세션에 멤버 정보가 없습니다.");
//            throw new SessionNotFoundException("세션에 멤버 정보가 없습니다.");
//        }
//
//        memberDTO.setId(memberVO.getId());
//        memberDTO.setMemberStatus("NO");
//
//        myPageService.softDeleteMember(memberDTO.toVO());
//
//        log.info("memberDTO", memberDTO);
//        return new RedirectView("/main");
//    }

//    @GetMapping("/member/video/my-page")
//    public String goToMyPageForm(Long id, Model model){
//        MemberVO memberVO = myPageService.getMember(id).orElseThrow();
//        model.addAttribute("member", memberVO);
//
//        return "/member/video/my-page";
//    }
//    //    http://localhost:10000/member/video/my-page?id=15


//    // 회원 탈퇴
//    @GetMapping("/member/video/my-page")
//    public void softDeleteMember(MemberDTO memberDTO, Model model) {
//        myPageService.softDeleteMember(memberDTO.toVO());
//        model.addAttribute("member", memberVO);
//    }

    @GetMapping(value = {"/member/video/my-page/delete"})
    public void goToReadForm(/*Long id, */Model model, HttpSession session){
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        model.addAttribute("member", memberVO);
    }

    // 회원 탈퇴
    @PostMapping("/member/video/my-page/delete")
    public RedirectView softDeleteMember(Long id) {
        myPageService.softDeleteMember(id);
        return new RedirectView("/main");
    }
//************************************************************************************************

    // 내 영상 작품 게시글 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/work/{page}")
    public MyWorkListDTO getMyVideoWorkList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyWorkAndFundingPagination myWorkAndFundingPagination, String postType) {

        return myPageService.getMyVideoWorkList(page, myWorkAndFundingPagination, memberId, postType);
    }

    // 내 영상 작품 구매자 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/work/{workPostId}/buyers/{page}")
    public MyWorkBuyerListDTO getMyVideoWorkBuyerList(@PathVariable("workPostId") Long workPostId
            , @PathVariable("page") int page, MySettingTablePagination mySettingTablePagination) {

        log.info("workPostId={}", workPostId);
        return myPageService.getMyVideoWorkBuyerList(page, mySettingTablePagination, workPostId);
    }

    // 내 영상 작품 발송 여부
    // UPDATE
    @ResponseBody
    @PutMapping("/members/video/my/work/buyers/sendStatus/update")
    public void updateWorkSendStatus(@RequestBody BuyWorkDTO buyWorkDTO) {
        myPageService.updateWorkSendStatus(buyWorkDTO.toVO());
    }

    // 내가 구매한 영상 작품 게시글 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/buy/work/{page}")
    public MyBuyWorkListDTO getMyBuyVideoWorkList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyWorkAndFundingPagination myWorkAndFundingPagination, String postType) {

        return myPageService.getMyBuyVideoWorkList(page, myWorkAndFundingPagination, memberId, postType);
    }

    // 내가 구매한 영상 작품 결제 내역 삭제
    // DELETE
    @ResponseBody
    @DeleteMapping("/members/video/my/buy/work/{id}")
    public void deleteBuyWorkPost(@PathVariable("id") Long id) {
        myPageService.deleteBuyWorkPost(id);
    }

//************************************************************************************************

    // 내 영상 펀딩 게시글 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/funding/{page}")
    public MyFundingListDTO getMyVideoFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyWorkAndFundingPagination myWorkAndFundingPagination, String postType) {

        log.info("memberId={}", memberId);
        return myPageService.getMyVideoFundingList(page, myWorkAndFundingPagination, memberId, postType);
    }

    // 내 영상 펀딩 구매자 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/funding/{fundingPostId}/buyers/{page}")
    public MyFundingBuyerListDTO getFundingBuyerList(@PathVariable("fundingPostId") Long fundingPostId
            , @PathVariable("page") int page, MySettingTablePagination mySettingTablePagination) {

        log.info("fundingPostId={}", fundingPostId);
        return myPageService.getMyFundingBuyerList(page, mySettingTablePagination, fundingPostId);
    }

    // 내 영상 펀딩 상품 발송 여부
    // UPDATE
    @ResponseBody
    @PutMapping("/members/video/my/funding/buyers/sendStatus/update")
    public void updateFundingSendStatus(@RequestBody BuyFundingProductDTO buyFundingProductDTO) {
        myPageService.updateFundingSendStatus(buyFundingProductDTO.toVO());
    }

    // 내가 결제한 펀딩 게시글 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/buy/funding/{page}")
    public MyBuyFundingListDTO getMyBuyFundingList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyWorkAndFundingPagination myWorkAndFundingPagination, String postType) {

        return myPageService.getMyBuyFundingList(page, myWorkAndFundingPagination, memberId, postType);
    }


//************************************************************************************************

    // 나의 모집 게시글 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/audition/{page}")
    public MyAuditionListDTO getMyVideoAuditionList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyAuditionPagination myAuditionPagination, String postType) {

        return myPageService.getMyVideoAuditionList(page, myAuditionPagination, memberId, postType);
    }

    // 나의 모집 지원자 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/video/my/audition/{auditionId}/applicants/{page}")
    public MyAuditionApplicantListDTO getMyVideoAuditionApplicantList(@PathVariable("auditionId") Long auditionId
            , @PathVariable("page") int page, MySettingTablePagination mySettingTablePagination) {

        log.info("auditionPostId={}", auditionId);
        return myPageService.getMyVideoAuditionApplicantList(page, mySettingTablePagination, auditionId);
    }

    // 나의 모집 확인여부
    // UPDATE
    @ResponseBody
    @PutMapping("/members/video/my/audition/applicants/confirm-status/update")
    public void updateConfirmStatus(@RequestBody AuditionApplicationDTO auditionApplicationDTO) {
        myPageService.updateConfirmStatus(auditionApplicationDTO.toVO());
    }

    // 내가 신청한 모집 게시글 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/video/my/application/audition/{page}")
    public MyApplicationAuditionListDTO getMyVideoApplicationAuditionList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyAuditionPagination myAuditionPagination, String postType) {

        return myPageService.getMyVideoApplicationAuditionList(page, myAuditionPagination, memberId, postType);
    }

//************************************************************************************************

    // 내 문의내역 목록
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/inquiry/{page}")
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(@PathVariable("memberId") Long memberId
            , @PathVariable("page") int page, MyWorkAndFundingPagination myWorkAndFundingPagination) {

        return myPageService.getMyInquiryHistoryList(page, myWorkAndFundingPagination, memberId);
    }

    // 내 문의 답변 조회
    // SELECT
    @ResponseBody
    @GetMapping("/members/inquiry/{inquiryId}/admin-answer")
    public Optional<AdminAnswerDTO> getAdminAnswerByInquiryId(@PathVariable("inquiryId") Long inquiryId) {
        return myPageService.getAdminAnswerByInquiryId(inquiryId);
    }

//************************************************************************************************



//************************************************************************************************

    // 파일 가져오기
    @GetMapping("/member/video/my/work/display")
    @ResponseBody
    public byte[] display(@RequestParam("fileName") String fileName) throws IOException {
        File file = new File("C:/upload", fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }

        return FileCopyUtils.copyToByteArray(file);
    }
}
