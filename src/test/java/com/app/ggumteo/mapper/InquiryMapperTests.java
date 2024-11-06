package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class InquiryMapperTests {
    @Autowired
    private InquiryMapper inquiryMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void testInquiryMapper() {
        InquiryDTO inquiryDTO = new InquiryDTO();
        AdminPagination adminPagination = new AdminPagination();
        inquiryDTO.setPostId(8L);

    }

    // 마이페이지 - 문의 내역
    @Test
    public void testSelectInquiryHistoryByMember() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = memberMapper.selectById(1L).get();
        workAndFundingPagination.setTotal(inquiryMapper.selectCountInquiryHistoryByMember(memberVO.getId()));
        workAndFundingPagination.progress();
        inquiryMapper.selectInquiryHistoryByMember(workAndFundingPagination, memberVO.getId())
                .stream().map(InquiryDTO::toString).forEach(log::info);
    }

    @Test
    public void testSelectAdminAnswer() {
        InquiryDTO inquiryDTO = null;
        inquiryDTO = inquiryMapper.selectById(20L).get();
        log.info(inquiryDTO.toString());
        inquiryMapper.selectAdminAnswerByInquiryId(inquiryDTO.getPostId()).stream().map(AdminAnswerDTO::toString).forEach(log::info);
    }
}
