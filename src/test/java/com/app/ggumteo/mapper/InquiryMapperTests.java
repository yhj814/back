package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class InquiryMapperTests {
    @Autowired
    private InquiryMapper inquiryMapper;

    @Test
    public void testInquiryMapper() {
        InquiryDTO inquiryDTO = new InquiryDTO();
        AdminPagination adminPagination = new AdminPagination();
        inquiryDTO.setPostId(8L);

    }

    // 마이페이지 - 문의 내역
    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void testSelectInquiryHistoryByMember() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = memberMapper.selectById(1L).get();
        log.info("memberVO={}", memberVO);
        workAndFundingPagination.setTotal(inquiryMapper.selectCountInquiryHistoryByMember(memberVO.getId()));
        log.info("workAndFundingPagination.getTotal()={}", workAndFundingPagination.getTotal());
        workAndFundingPagination.progress();
        log.info("memberVO={}", memberVO);
        log.info("inquiryMapper={}", inquiryMapper);
        inquiryMapper.selectInquiryHistoryByMember(workAndFundingPagination, memberVO.getId())
                .stream().map(InquiryDTO::toString).forEach(log::info);

        log.info("inquiryMapper={}", inquiryMapper);
        log.info("inquiryMapper.selectCountInquiryHistoryByMember(memberVO.getId())={}", inquiryMapper.selectCountInquiryHistoryByMember(memberVO.getId()));
        log.info("inquiryMapper={}", inquiryMapper.selectInquiryHistoryByMember(workAndFundingPagination, memberVO.getId()).stream());
        log.info("inquiryMapper.selectInquiryHistoryByMember={}", inquiryMapper.selectInquiryHistoryByMember(workAndFundingPagination, memberVO.getId()));
    }
}
