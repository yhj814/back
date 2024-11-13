package com.app.ggumteo.repository;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.repository.member.MemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class InquiryDAOTests {
    @Autowired
    private InquiryDAO inquiryDAO;
    @Autowired
    private MemberDAO memberDAO;

    // 마이페이지 - 문의 내역
    @Test
    public void testSelectInquiryHistoryByMember() {
        MemberVO memberVO = null;
        MyWorkAndFundingPagination myWorkAndFundingPagination = new MyWorkAndFundingPagination();
        memberVO = memberDAO.findById(1L).get();
        myWorkAndFundingPagination.setTotal(inquiryDAO.getTotalInquiryHistoryByMember(memberVO.getId()));
        myWorkAndFundingPagination.progress();
        inquiryDAO.findInquiryHistoryByMember(myWorkAndFundingPagination, memberVO.getId())
                .stream().map(InquiryDTO::toString).forEach(log::info);
    }
}
