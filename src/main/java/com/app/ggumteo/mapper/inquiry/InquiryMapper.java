package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface InquiryMapper {
    // 문의하기
    void insertInquiry(PostDTO postDTO);

    void insertInquiryToTblInquiry(PostDTO postDTO);

    // 마지막 삽입된 ID를 가져오는 메서드
    Long getLastInsertId();

    // 페이징 처리된 전체 문의사항 가져오기
    List<InquiryDTO> selectAll(@Param("pagination")AdminPagination pagination);

    // 총 문의사항 개수 조회
    int countTotal();
}
