package com.app.ggumteo.service;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.alarm.MyAlarmListDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.MyAuditionApplicantListDTO;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.member.MemberProfileMapper;
import com.app.ggumteo.pagination.MyAlarmPagination;
import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MySettingTablePagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.repository.alarm.AlarmDAO;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@SpringBootTest
@Slf4j

public class MyPageServiceTests {
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private AlarmDAO alarmDAO;
    @Autowired
    private MemberProfileMapper memberProfileMapper;

    @Test
    public void getMemberProfileByMemberId() {
        MemberProfileDTO memberProfileDTO = new MemberProfileDTO();
        memberProfileDTO.setMemberId(9L);

        Optional<MemberProfileVO> foundMemberInfo = myPageService.getMemberProfileByMemberId(memberProfileDTO.getMemberId());
        foundMemberInfo.map(MemberProfileVO::toString).ifPresent(log::info);
    }

    @Test
    public void testGetMyVideoWorkList() {
        MemberVO memberVO = null;
        MyWorkAndFundingPagination myWorkAndFundingPagination = new MyWorkAndFundingPagination();
        memberVO = myPageService.getMember(4L).get();
        myWorkAndFundingPagination.setTotal(myPageService.getMyVideoWorkPostsTotal(memberVO.getId(), PostType.WORKVIDEO.name()));
        myWorkAndFundingPagination.progress();
        MyWorkListDTO myWorkPosts = myPageService.getMyVideoWorkList
                (1, myWorkAndFundingPagination, memberVO.getId(), PostType.WORKVIDEO.name());

        log.info(" myWorkPosts.toString()-test={}", myWorkPosts.toString());
    }

    @Test
    public void testGetMyVideoWorkBuyerList() {
        WorkDTO workDTO = null;
        MySettingTablePagination mySettingTablePagination = new MySettingTablePagination();
        workDTO = myPageService.getWork(5L, PostType.WORKVIDEO.name()).get();
        mySettingTablePagination.setTotal(myPageService.getMyVideoWorkBuyersTotal(workDTO.getId()));
        mySettingTablePagination.progress();
        MyWorkBuyerListDTO myWorkBuyers = myPageService.getMyVideoWorkBuyerList(1, mySettingTablePagination, workDTO.getId());

        log.info("myWorkBuyers.toString()-test={}", myWorkBuyers.toString());
    }

    @Test
    public void testUpdateWorkSendStatus() {
        BuyWorkDTO buyWorkDTO = new BuyWorkDTO();
        buyWorkDTO.setId(1L);
        buyWorkDTO.setWorkSendStatus("YES");

        myPageService.updateWorkSendStatus(buyWorkDTO.toVO());
    }

    @Test
    public void testDeleteBuyWorkPost() {
        myPageService.deleteBuyWorkPost(12L);
    }

    @Test
    public void testGetMyFundingPosts() {
        MemberVO memberVO = null;
        MyWorkAndFundingPagination myWorkAndFundingPagination = new MyWorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        myWorkAndFundingPagination.setTotal(myPageService.getMyFundingPostsTotal(memberVO.getId(), PostType.FUNDINGVIDEO.name()));
        myWorkAndFundingPagination.progress();
        MyFundingListDTO myFundingPosts = myPageService.getMyVideoFundingList(1, myWorkAndFundingPagination, memberVO.getId(), PostType.FUNDINGVIDEO.name());

        log.info("GetMyFundingPosts-test={}", myFundingPosts.toString());
    }

    @Test
    public void testGetMyFundingBuyerList() {
        FundingDTO fundingDTO = null;
        MySettingTablePagination mySettingTablePagination = new MySettingTablePagination();
        fundingDTO = myPageService.getFunding(9L, PostType.FUNDINGVIDEO.name()).get();
        mySettingTablePagination.setTotal(myPageService.getMyFundingPostBuyersTotal(fundingDTO.getId()));
        mySettingTablePagination.progress();
        MyFundingBuyerListDTO myFundingBuyers = myPageService.getMyFundingBuyerList(1, mySettingTablePagination, fundingDTO.getId());

        log.info("GetMyFundingBuyerList-test={}", myFundingBuyers.toString());
  }

    @Test
    public void testUpdateFundingSendStatus() {
    BuyFundingProductDTO buyFundingProductDTO = new BuyFundingProductDTO();
    buyFundingProductDTO.setId(7L);
    buyFundingProductDTO.setFundingSendStatus("YES");

    myPageService.updateFundingSendStatus(buyFundingProductDTO.toVO());
    }

    @Test
    public void testGetFundingListPaidByMemberTotal() {
        MemberVO memberVO = null;
        MyWorkAndFundingPagination myWorkAndFundingPagination = new MyWorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        myWorkAndFundingPagination.setTotal(myPageService.getMyBuyFundingListTotal(memberVO.getId(), PostType.FUNDINGVIDEO.name()));
        myWorkAndFundingPagination.progress();
        MyBuyFundingListDTO fundingPostsPaidByMember = myPageService
                .getMyBuyFundingList(1, myWorkAndFundingPagination, memberVO.getId(), PostType.FUNDINGVIDEO.name());

        log.info(fundingPostsPaidByMember.toString());
    }

    @Test
    public void testGetMyInquiryHistoryList() {
        MemberVO memberVO = null;
        MyWorkAndFundingPagination myWorkAndFundingPagination = new MyWorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        myWorkAndFundingPagination.setTotal(myPageService.getMyInquiryHistoriesTotal(memberVO.getId()));
        myWorkAndFundingPagination.progress();
        MyInquiryHistoryListDTO myInquiryHistories = myPageService.getMyInquiryHistoryList(1, myWorkAndFundingPagination, memberVO.getId());

        log.info(myInquiryHistories.toString());
    }

    @Test
    public void testGetAdminAnswerByInquiryId() {
        InquiryDTO inquiryDTO = null;
        inquiryDTO = myPageService.getInquiry(20L).get();
        log.info(inquiryDTO.toString());
        myPageService.getAdminAnswerByInquiryId(inquiryDTO.getPostId())
                .stream().map(AdminAnswerDTO::toString).forEach(log::info);
    }

    @Test
    public void testGetAudition() {
    AuditionDTO auditionDTO = new AuditionDTO();
        auditionDTO.setId(33L);
        auditionDTO.setPostType(PostType.AUDITIONVIDEO.name());

    Optional<AuditionDTO> foundAudition = myPageService.getAudition(auditionDTO.getId(), auditionDTO.getPostType());
        foundAudition.map(AuditionDTO::toString).ifPresent(log::info);
}

    @Test
    public void testGetMyVideoAuditionApplicantList() {
        AuditionDTO auditionDTO = null;
        MySettingTablePagination mySettingTablePagination = new MySettingTablePagination();
        auditionDTO = myPageService.getAudition(33L, PostType.AUDITIONVIDEO.name()).get();
        mySettingTablePagination.setTotal(myPageService.getMyVideoAuditionApplicantsTotal(auditionDTO.getId()));
        mySettingTablePagination.progress();
        MyAuditionApplicantListDTO myAuditionApplicants = myPageService.getMyVideoAuditionApplicantList(1, mySettingTablePagination, auditionDTO.getId());

        log.info("myAuditionApplicants-test={}", myAuditionApplicants);
    }

    @Test
    public void testUpdateConfirmStatus() {
        AuditionApplicationDTO auditionApplicationDTO = new AuditionApplicationDTO();
        auditionApplicationDTO.setId(1L);
        auditionApplicationDTO.setConfirmStatus("YES");

        myPageService.updateConfirmStatus(auditionApplicationDTO.toVO());
    }

    @Test
    public void testUpdateMemberProfile() {
        MemberProfileDTO memberProfileDTO = new MemberProfileDTO();
        memberProfileDTO.setMemberId(5L);
        memberProfileDTO.setProfileName("수정된 이름");
        memberProfileDTO.setProfileNickName("수정된 닉네임");
        memberProfileDTO.setProfileAge(30);
        memberProfileDTO.setProfileGender("남성");
        memberProfileDTO.setProfileEmail("000test@gmail.com");
        memberProfileDTO.setProfilePhone("01045678912");
        memberProfileDTO.setProfileEtc("추가추가");

        myPageService.updateMemberProfileByMemberId(memberProfileDTO.toVO());

        log.info("memberProfileDTO={}", memberProfileDTO);
    }

    @Test
    public void testSoftDeleteMember() {
        myPageService.softDeleteMember(5L);
    }

    @Test
    public void testGetMyAlarmsByMemberProfileId() {
        MemberProfileVO memberProfileVO = null;
        MyAlarmPagination myAlarmPagination = new MyAlarmPagination();
        memberProfileVO = memberProfileMapper.selectById(1L).get();
        myAlarmPagination.setTotal(alarmDAO.getAlarmTotal(memberProfileVO.getId(), AlarmSubType.VIDEO.name()));
        myAlarmPagination.progress();
        MyAlarmListDTO myAlarms = myPageService.getMyAlarmsByMemberProfileId(1, myAlarmPagination, memberProfileVO.getId(),AlarmSubType.VIDEO.name());

        log.info(" myAlarms.toString()-test={}", myAlarms.toString());
        log.info(" myAlarmPagination.getTotal()={}", myAlarmPagination.getTotal());
        log.info(" myAlarmPagination.getRowCount()={}", myAlarmPagination.getRowCount());


    }

    @Test
    public void testGetMyVideoWorkPostsTotal() {
        MemberVO memberVO = null;
        memberVO = myPageService.getMember(1L).get();
        int myVideoWorkPostsTotal = myPageService.getMyVideoWorkPostsTotal(memberVO.getId(), PostType.WORKVIDEO.name());

        log.info("myVideoWorkPostsTotal={}", myVideoWorkPostsTotal);

    }

}
