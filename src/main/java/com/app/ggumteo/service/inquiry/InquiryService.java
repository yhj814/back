package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;


public interface InquiryService {

    void writeInquiry(PostDTO postDTO);

    List<InquiryDTO> getList(AdminPagination pagination);

    int getTotal();
}