package com.app.ggumteo.repository.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
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

    // 문의 목록 조회 메서드
    public List<InquiryDTO> getInquiryList(int offset, int limit) {
        return inquiryMapper.getInquiryList(offset, limit);
    }

    // 총 문의 수 조회 메서드
    public int getInquiryCount() {
        return inquiryMapper.getInquiryCount();
    }
}


