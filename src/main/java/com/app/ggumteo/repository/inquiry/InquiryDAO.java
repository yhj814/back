package com.app.ggumteo.repository.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class InquiryDAO {
    private final InquiryMapper inquiryMapper;

    public void insertInquiry(PostDTO postDTO) {
        inquiryMapper.insertInquiry(postDTO);
    }

    public Long getLastInsertId() {
        return inquiryMapper.getLastInsertId();
    }

    public void insertInquiryToTblInquiry(PostDTO postDTO) {
        inquiryMapper.insertInquiryToTblInquiry(postDTO);
    }

    public List<InquiryDTO> findAllInquiry(AdminPagination pagination) {
        return inquiryMapper.selectInquiryAll(pagination);
    }

    public int getTotalInquiry() {
        return inquiryMapper.countInquiries();
    }
}


