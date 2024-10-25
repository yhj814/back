package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.InquiryDetailVO;

import java.util.List;


public interface InquiryService {
    // 문의작성
    void createInquiry(InquiryDTO inquiryDTO, Long memberId);

}