package com.app.ggumteo.repository.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class InquiryDAO {
    private final InquiryMapper inquiryMapper;

    public void insertInquiry(PostDTO postDTO) {
        inquiryMapper.insertInquiry(postDTO);
    }

    public Long getLastInsertId() {
        return inquiryMapper.getLastInsertId();
    }

    public void insertInquiryToTblInquiry(PostDTO postDTO) {
        Long lastId =getLastInsertId();
        postDTO.setId(lastId);
        inquiryMapper.insertInquiryToTblInquiry(postDTO);
    }

    public List<InquiryDTO> findAllInquiry(AdminPagination pagination) {
        log.info("Querying inquiries with pagination: {}", pagination);
        List<InquiryDTO> inquiries = inquiryMapper.selectInquiryAll(pagination);
        log.info("Inquiries found: {}", inquiries);
        return inquiries;
    }

    public int getTotalInquiry() {
        return inquiryMapper.countInquiries();
    }
}


