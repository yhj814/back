package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;


public interface InquiryService {

    void createInquiry(InquiryDTO inquiryDTO, Long memberId);
}