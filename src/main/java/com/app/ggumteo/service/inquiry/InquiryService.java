package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;


public interface InquiryService {
    // 문의사항 작성
    void writeInquiry(PostDTO postDTO);

    // 페이징 처리된 전체 문의사항 조회
    List<InquiryDTO> getInquiries(AdminPagination pagination);

    // 총 문의사항 개수 조회
    int getTotalInquiries();
}
