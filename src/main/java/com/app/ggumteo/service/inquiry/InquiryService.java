package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;
import java.util.Map;


public interface InquiryService {

    // 문의사항 작성
    void writeInquiry(PostDTO postDTO);

    // 정렬과 검색어가 적용된 페이징 처리된 전체 문의사항 조회
    List<InquiryDTO> getInquiries(AdminPagination pagination, String order, String searchKeyword);

    // 정렬과 검색어가 적용된 총 문의사항 개수 조회
    int getTotalInquiries(String order, String searchKeyword);

    // 문의 상태 업데이트 및 답변 등록 후 답변 내용과 생성일 반환
    Map<String, Object> registerAnswer(Long inquiryId, String answerContent);

    // 문의 삭제
    void deleteSelectedInquiries(List<Long> ids);
}



