package com.app.ggumteo.service;

import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.FundingMapperTests;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
        workAndFundingPagination.setTotal(myPageService.getMyFundingPostsTotal(memberVO.getId()));
        workAndFundingPagination.progress();
        MyFundingListDTO myFundingPosts = myPageService.getMyFundingList(1, workAndFundingPagination, memberVO.getId());

        log.info("GetMyFundingPosts-test={}", myFundingPosts.toString());
    }

    @Test
    public void testGetMyFundingBuyerList() {
        FundingDTO fundingDTO = null;
        SettingTablePagination settingTablePagination = new SettingTablePagination();
        fundingDTO = myPageService.getFunding(9L).get();
        settingTablePagination.setTotal(myPageService.getMyFundingPostBuyerTotal(fundingDTO.getId()));
        settingTablePagination.progress();
        MyFundingBuyerListDTO myFundingBuyers = myPageService.getMyFundingBuyerList(1, settingTablePagination, fundingDTO.getId());

        log.info("GetMyFundingBuyerList-test={}", myFundingBuyers.toString());
  }

}
