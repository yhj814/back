package com.app.ggumteo.service.inquiry;


import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class InquiryServiceImpl implements InquiryService {

    private final InquiryDAO inquiryDAO;

    // 문의사항 작성
    @Override
    public void writeInquiry(PostDTO postDTO) {
        inquiryDAO.insertInquiry(postDTO);

        Long lastInsertId = inquiryDAO.getLastInsertId();
        postDTO.setId(lastInsertId);
        inquiryDAO.insertInquiryToTblInquiry(postDTO);

        log.info("문의사항 작성 완료, ID: {}", lastInsertId);
    }

    // 정렬과 검색어가 적용된 페이징 처리된 전체 문의사항 조회
    @Override
    public List<InquiryDTO> getInquiries(AdminPagination pagination, String order, String searchKeyword) {
        return inquiryDAO.selectAll(pagination, order, searchKeyword);
    }

    // 정렬과 검색어가 적용된 총 문의사항 개수 조회
    @Override
    public int getTotalInquiries(String order, String searchKeyword) {
        return inquiryDAO.countTotal(order, searchKeyword);
    }

    // 문의 상태 업데이트 및 답변 등록 후 답변 내용과 생성일 반환
    @Override
    public Map<String, Object> registerAnswer(Long inquiryId, String answerContent) {
        inquiryDAO.updateInquiryStatus(inquiryId);
        return inquiryDAO.insertAdminAnswer(inquiryId, answerContent);
    }

    // 문의 삭제
    @Override
    public void deleteSelectedInquiries(List<Long> ids) {
        inquiryDAO.deleteSelectedInquiries(ids);
    }
}





