package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;

import java.util.List;


public interface InquiryService {
    // 문의하기
    void createInquiry(InquiryDTO inquiryDTO, Long memberId);



}