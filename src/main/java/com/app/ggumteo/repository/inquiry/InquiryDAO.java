package com.app.ggumteo.repository.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.InquiryVO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InquiryDAO {
    private final InquiryMapper inquiryMapper;

    // 문의 사항 저장
    public void saveInquiry(InquiryVO inquiry) {
        inquiryMapper.insertInquiry(inquiry);
    }

}

