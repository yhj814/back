package com.app.ggumteo.repository.inquiry;

import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Slf4j
public class InquiryDAO {
    private final InquiryMapper inquiryMapper;

    // 문의사항 삽입
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

    // 페이징 처리된 전체 문의사항 조회 (정렬 및 검색 조건 포함)
    public List<InquiryDTO> selectAll(AdminPagination pagination, String order, String searchKeyword) {
        return inquiryMapper.selectAll(pagination, order, searchKeyword);
    }

    // 총 문의사항 개수 조회 (정렬 및 검색 조건 포함)
    public int countTotal(String order, String searchKeyword) {
        return inquiryMapper.countTotal(order, searchKeyword);
    }

    // 문의 상태 업데이트
    public void updateInquiryStatus(Long inquiryId) {
        inquiryMapper.updateInquiryStatus(inquiryId);
    }

    // 답변 등록 후 답변 내용과 생성일 반환
    public Map<String, Object> insertAdminAnswer(Long inquiryId, String answerContent) {
        inquiryMapper.insertAdminAnswer(inquiryId, answerContent); // answerDate는 제거됨
        return inquiryMapper.getAnswerContentAndDate(inquiryId);
    }

    // 문의사항과 관련된 답변, 문의, 게시물 순차적으로 삭제
    public void deleteSelectedInquiries(List<Long> ids) {
        // 각 테이블의 데이터를 순차적으로 삭제
        // 1단계: 답변 삭제
        inquiryMapper.deleteFromAdminAnswer(ids);

        // 2단계: 문의 삭제
        inquiryMapper.deleteFromInquiry(ids);

        // 3단계: 게시물 삭제
        inquiryMapper.deleteFromPost(ids);
    }

    // 마이페이지 - 문의 내역 목록 조회
    public List<InquiryDTO> findInquiryHistoryByMember(WorkAndFundingPagination workAndFundingPagination, Long memberId) {
       return inquiryMapper.selectInquiryHistoryByMember(workAndFundingPagination, memberId);
    }

    // 마이페이지 - 문의 내역 전체 갯수
    public int getTotalInquiryHistoryByMember(Long memberId) {
        return inquiryMapper.selectCountInquiryHistoryByMember(memberId);
    };

    // 문의 내역 조회
    public Optional<InquiryDTO> findById(Long postId) {
        return inquiryMapper.selectById(postId);
    };

    // 마이페이지 - 문의 내역 관리자 답변
    public Optional<AdminAnswerDTO> findAdminAnswerByInquiryId(Long inquiryId) {
        return inquiryMapper.selectAdminAnswerByInquiryId(inquiryId);
    };
}









