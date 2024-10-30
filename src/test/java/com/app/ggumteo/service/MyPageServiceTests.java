package com.app.ggumteo.service;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.FundingMapperTests;
import com.app.ggumteo.pagination.MyPagePagination;
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
        MyPagePagination myPagePagination = new MyPagePagination();
        memberVO = myPageService.getMember(1L).get();
        myPagePagination.setTotal(myPageService.getTotal(memberVO.getId()));
        myPagePagination.progress();
        MyFundingListDTO fundingList = myPageService.getMyFundingList(1, myPagePagination, memberVO.getId());

        log.info("service-test={}", fundingList.toString());

//        List<FundingDTO> fundingPosts = myPageService.getMyFundingList(1L);
////        fundingMapper.selectByMemberId(1L).stream().map(FundingDTO::toString).forEach(log::info);
//
//        for (FundingDTO fundingDTO : fundingPosts) {
//            log.info("{}", fundingDTO);
//        }
    }
}
