package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.domain.alarm.MyAlarmListDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.MyApplicationAuditionListDTO;
import com.app.ggumteo.domain.audition.MyAuditionApplicantListDTO;
import com.app.ggumteo.domain.audition.MyAuditionListDTO;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.pagination.MyAlarmPagination;
import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MySettingTablePagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.service.alarm.AlarmService;
import com.app.ggumteo.service.member.KakaoService;
import com.app.ggumteo.service.myPage.MyPageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {
    private final MyPageService myPageService;
    private final KakaoService kakaoService;
    private final AlarmService alarmService;

    @GetMapping("/member/video/my-page")
    public String goToReadForm(Model model, HttpSession session, @RequestParam(value = "category", required = false) String type) {
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfileDTO = (MemberProfileDTO) session.getAttribute("memberProfile");
        model.addAttribute("member", memberVO);

        boolean isLoggedIn = memberVO != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        log.info("마이페이지 memberProfileDTO={}", memberProfileDTO);

        if (type != null) {
            model.addAttribute("type", type);
        }

        return "member/video/my-page";
    }

    // 내 정보 조회
    // SELECT
    @ResponseBody
    @GetMapping("/members/{memberId}/profile")
    public Optional<MemberProfileVO> getMemberProfileByMemberId(@PathVariable("memberId") Long memberId) {
        return myPageService.getMemberProfileByMemberId(memberId);
    }

    // 내 정보 수정
    // UPDATE
    @ResponseBody
    @PutMapping("/members/profile/update")
    public void updateMemberProfileByMemberId(@RequestBody MemberProfileDTO memberProfileDTO) {
        myPageService.updateMemberProfileByMemberId(memberProfileDTO.toVO());
    }

    // 회원 탈퇴
    @GetMapping("/member/video/my-page/delete")
    public RedirectView softDeleteMember(HttpSession session, HttpServletResponse response) {
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        Long id = memberVO.getId();

        String token = (String) session.getAttribute("kaKaoToken");
        log.info("로그아웃 시 세션에서 가져온 kaKaoToken: {}", token);

        if (token != null) {
            boolean isWithDraw = kakaoService.kakaoLogout(token);

            if (isWithDraw) {
                session.removeAttribute("kaKaoToken");
                session.removeAttribute("member");
                session.invalidate();
                log.info("탈퇴 성공");

            } else {
                log.error("탈퇴 실패");
            }
        } else {
            log.error("세션에 저장된 토큰이 없습니다.");
        }

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

    /**
     * 현재 로그인한 회원의 읽지 않은 알림을 조회합니다.
     *
     * @param session HTTP 세션
     * @return 읽지 않은 알림 목록
     */
    @GetMapping("/members/video/alarm/unread")
    @ResponseBody
    public ResponseEntity<List<AlarmDTO>> getUnreadAlarmsByCurrentMemberProfile(HttpSession session) {
        MemberProfileDTO memberProfileDTO = (MemberProfileDTO) session.getAttribute("memberProfile");
        if (memberProfileDTO != null) {
            List<AlarmDTO> latestAlarms = alarmService.getUnreadAlarmsByMemberId(memberProfileDTO.getId());
            log.info("알람 memberProfileDTO.getId()={}", memberProfileDTO.getId());
            log.info("알람 latestAlarms={}",latestAlarms);
            return ResponseEntity.ok(latestAlarms);
        } else {
            log.info("알람 else memberDTO={}", memberProfileDTO);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 내 알림 목록
    // SELECT
    @ResponseBody
    @GetMapping("/member/video/alarms/{page}")
    public MyAlarmListDTO getMyAlarmsByMemberProfileId(HttpSession session
            , @PathVariable("page") int page, MyAlarmPagination myAlarmPagination, String subType) {

        MemberProfileDTO memberProfileDTO = (MemberProfileDTO) session.getAttribute("memberProfile");

        if (memberProfileDTO == null) {
            throw new IllegalStateException("로그인 하지 않은 상태입니다.");
        }

        Long memberProfileId = memberProfileDTO.getId();

        return myPageService.getMyAlarmsByMemberProfileId(page, myAlarmPagination, memberProfileId, subType);
    }


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
