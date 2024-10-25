package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.InquiryDetailVO;
import com.app.ggumteo.domain.inquiry.InquiryVO;
import com.app.ggumteo.domain.post.PostVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryMapper {
    // 문의하기
    void insertInquiry(InquiryVO inquiry);

    // 문의목록
    List<InquiryDetailVO> selectInquiries();

}
