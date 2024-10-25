package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;


public interface InquiryService {
    // 문의하기
    void createInquiry(InquiryDTO inquiryDTO, Long memberId);
}