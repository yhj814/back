package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {
    // 문의사항 삽입
    void insertInquiry(PostDTO postDTO);

    // tbl_inquiry에 문의사항 삽입
    void insertInquiryToTblInquiry(PostDTO postDTO);

    // 마지막 삽입된 ID를 가져오는 메서드
    Long getLastInsertId();

    // 페이징 처리된 전체 문의사항 가져오기
    List<InquiryDTO> selectAll(@Param("pagination") AdminPagination pagination);

    // 총 문의사항 개수 조회
    int countTotal();

    // 문의사항 상태 업데이트 (inquiry_status를 'YES'로 변경)
    void updateInquiryStatus(@Param("inquiryId") Long inquiryId);

    // 답변 등록 (tbl_admin_answer 테이블에 답변 내용 추가)
    void insertAdminAnswer(@Param("inquiryId") Long inquiryId, @Param("answerContent") String answerContent,@Param("answerDate") String answerDate);

    // 답변 내용 및 생성일 조회
    Map<String, Object> getAnswerContentAndDate(@Param("inquiryId") Long inquiryId);
}




