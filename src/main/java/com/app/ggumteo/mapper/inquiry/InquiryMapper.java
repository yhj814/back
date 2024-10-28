package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {
    // 문의하기
    void insertInquiry(PostDTO postDTO);
    void insertInquiryToTblInquiry(PostDTO postDTO);
    // 마지막 삽입된 ID를 가져오는 메서드
    Long getLastInsertId();
}
