package com.app.ggumteo.mapper.inquiry;

import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface InquiryMapper {

    // 문의사항 삽입 (tbl_post에 데이터 추가)
    void insertInquiry(PostDTO postDTO);

    // tbl_inquiry에 문의사항 삽입 (id와 상태 추가)
    void insertInquiryToTblInquiry(PostDTO postDTO);

    // 마지막 삽입된 ID를 가져오는 메서드
    Long getLastInsertId();

    // 페이징 처리된 전체 문의사항 가져오기 (정렬 및 검색 조건 포함)
    List<InquiryDTO> selectAll(
            @Param("pagination") AdminPagination pagination, // 페이징 정보
            @Param("order") String order, // 정렬 옵션 (문의일순, 답변일순, 미답변)
            @Param("searchKeyword") String searchKeyword // 검색어
    );

    // 총 문의사항 개수 조회 (정렬 및 검색 조건 포함)
    int countTotal(
            @Param("order") String order, // 정렬 옵션 (문의일순, 답변일순, 미답변)
            @Param("searchKeyword") String searchKeyword // 검색어
    );

    // 문의사항 상태 업데이트 (inquiry_status를 'YES'로 변경)
    void updateInquiryStatus(@Param("inquiryId") Long inquiryId);


    // 답변 등록 (tbl_admin_answer 테이블에 답변 내용 추가)
    void insertAdminAnswer(
            @Param("inquiryId") Long inquiryId,
            @Param("answerContent") String answerContent
    );

    // 답변 내용 및 생성일 조회
    Map<String, Object> getAnswerContentAndDate(@Param("inquiryId") Long inquiryId);

    // 문의 답변 삭제
    void deleteFromAdminAnswer(@Param("list") List<Long> ids);

    // 문의 테이블 문의 삭제
    void deleteFromInquiry(@Param("list") List<Long> ids);

    // post테이블에 postType이 INQUIRY 인거 삭제
    void deleteFromPost(@Param("list") List<Long> ids);

    // 마이페이지 - 문의 내역 조회
    public List<InquiryDTO> selectInquiryHistoryByMember(
            @Param("workAndFundingPagination") WorkAndFundingPagination workAndFundingPagination
            , @Param("memberId") Long memberId);

    // 마이페이지 - 문의 내역 전체 갯수
    public int selectCountInquiryHistoryByMember(Long memberId);

    // 마이페이지 - 문의 내역 관리자 답변
    public Optional<AdminAnswerDTO> selectAdminAnswer(Long id);
}








