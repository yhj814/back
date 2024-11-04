package com.app.ggumteo.service;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j

public class MyPageServiceTests {
    @Autowired
    private MyPageService myPageService;

    @Test
    public void testGetMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);

        Optional<MemberVO> foundMemberInfo = myPageService.getMember(memberDTO.getId());
        foundMemberInfo.map(MemberVO::toString).ifPresent(log::info);
    }

    @Test
    public void testGetMyFundingPosts() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        workAndFundingPagination.setTotal(myPageService.getMyFundingPostsTotal(memberVO.getId(), PostType.VIDEO.name()));
        workAndFundingPagination.progress();
        MyFundingListDTO myFundingPosts = myPageService.getMyVideoFundingList(1, workAndFundingPagination, memberVO.getId(), PostType.VIDEO.name());

        log.info("GetMyFundingPosts-test={}", myFundingPosts.toString());
    }

    @Test
    public void testGetMyFundingBuyerList() {
        FundingDTO fundingDTO = null;
        SettingTablePagination settingTablePagination = new SettingTablePagination();
        fundingDTO = myPageService.getFunding(9L, PostType.VIDEO.name()).get();
        settingTablePagination.setTotal(myPageService.getMyFundingPostBuyersTotal(fundingDTO.getId()));
        settingTablePagination.progress();
        MyFundingBuyerListDTO myFundingBuyers = myPageService.getMyFundingBuyerList(1, settingTablePagination, fundingDTO.getId());

        log.info("GetMyFundingBuyerList-test={}", myFundingBuyers.toString());
  }

    @Test
    public void testCheckFundingSendStatus() {
    BuyFundingProductDTO buyFundingProductDTO = new BuyFundingProductDTO();
    buyFundingProductDTO.setId(7L);
    buyFundingProductDTO.setFundingSendStatus("YES");

    myPageService.updateFundingSendStatus(buyFundingProductDTO.toVO());
    }

    @Test
    public void testGetFundingListPaidByMemberTotal() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        workAndFundingPagination.setTotal(myPageService.getMyBuyFundingListTotal(memberVO.getId(), PostType.VIDEO.name()));
        workAndFundingPagination.progress();
        MyBuyFundingListDTO fundingPostsPaidByMember = myPageService
                .getMyBuyFundingList(1, workAndFundingPagination, memberVO.getId(), PostType.VIDEO.name());

        log.info(fundingPostsPaidByMember.toString());
    }

    @Test
    public void testGetMyInquiryHistoryList() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        workAndFundingPagination.setTotal(myPageService.getMyInquiryHistoriesTotal(memberVO.getId()));
        workAndFundingPagination.progress();
        MyInquiryHistoryListDTO myInquiryHistories = myPageService.getMyInquiryHistoryList(1, workAndFundingPagination, memberVO.getId());

        log.info(myInquiryHistories.toString());
    }

}
