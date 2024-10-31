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

    // 문의하기 삽입
    public void insertInquiry(PostDTO postDTO) {
        inquiryMapper.insertInquiry(postDTO);
    }

    // 마지막 삽입된 ID 가져오기
    public Long getLastInsertId() {
        return inquiryMapper.getLastInsertId();
    }

    // tbl_inquiry에 삽입
    public void insertInquiryToTblInquiry(PostDTO postDTO) {
        Long lastId = getLastInsertId();
        postDTO.setId(lastId);
        inquiryMapper.insertInquiryToTblInquiry(postDTO);
    }

    // 페이징 처리된 전체 문의사항 조회
    public List<InquiryDTO> selectAll(AdminPagination pagination) {
        return inquiryMapper.selectAll(pagination);
    }

    // 총 문의사항 개수 조회
    public int countTotal() {
        return inquiryMapper.countTotal();
    }
}



