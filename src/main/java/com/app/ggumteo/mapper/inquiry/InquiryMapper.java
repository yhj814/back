package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryVO;
import com.app.ggumteo.domain.post.PostVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryMapper {
    void insertInquiry(InquiryVO inquiry);
}
