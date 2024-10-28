package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {
    // 문의하기
    void insertInquiry(InquiryDTO inquiryDTO);

}
