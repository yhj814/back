package com.app.ggumteo.service.myPage;

import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;

import java.util.Optional;

public interface MyPageService {
    // 회원 정보 조회
    public Optional<MemberVO> getMember(Long id);

//====작품 ====================================================

    // 내 작품 게시물 전체 조회
    public MyWorkListDTO getMyVideoWorkList(
            int page , WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType);

    // 내 작품 게시물 전체 개수
    public int getMyVideoWorkPostsTotal(Long memberId, String postType);

    // 작품 정보 조회
    public Optional<WorkDTO> getWork(Long id, String postType);

    // 작품 구매자 목록 조회
    public MyWorkBuyerListDTO getMyVideoWorkBuyerList
    (int page, SettingTablePagination settingTablePagination , Long workPostId);

    // 내 작품 게시물 하나의 구매자 전체 갯수
    public int getMyVideoWorkBuyersTotal(Long workPostId);

    // 작품 발송 여부 체크
    public void updateWorkSendStatus(BuyWorkVO buyWorkVO);

    // 내가 구매한 작품 목록 조회
    public MyBuyWorkListDTO getMyBuyVideoWorkList(
            int page, WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType);

    // 내가 구매한 작품 목록 전체 갯수
    public int getMyBuyWorkListTotal(Long memberId, String postType);

//====펀딩 ====================================================

    // 내 펀딩 게시물 전체 조회
    public MyFundingListDTO getMyVideoFundingList(
            int page , WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType);

    // 내 펀딩 게시물 전체 개수
    public int getMyFundingPostsTotal(Long memberId, String postType);

    // 펀딩 정보 조회
    public Optional<FundingDTO> getFunding(Long id, String postType);

    // 펀딩 구매자 목록 조회
    public MyFundingBuyerListDTO getMyFundingBuyerList(
           int page, SettingTablePagination settingTablePagination, Long fundingPostId);

    // 내 펀딩 게시물 하나의 구매자 전체 갯수
    public int getMyFundingPostBuyersTotal(Long fundingPostId);

    // 펀딩상품 발송여부 체크
    public void updateFundingSendStatus(BuyFundingProductVO buyFundingProductVO);

    // 내가 결제한 펀딩 목록 조회
    public MyBuyFundingListDTO getMyBuyFundingList(
            int page, WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType);

    // 내가 결제한 펀딩 목록 전체 갯수
    public int getMyBuyFundingListTotal(Long memberId, String postType);

//====문의내역 ====================================================

    // 마이페이지 - 문의 내역 조회
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(
            int page, WorkAndFundingPagination workAndFundingPagination, Long memberId);

    // 마이페이지 - 문의 내역 전체 갯수
    public int getMyInquiryHistoriesTotal(Long memberId);

    // 문의 내역 조회
    public Optional<InquiryDTO> getInquiry(Long postId);

    // 마이페이지 - 문의 내역 관리자 답변
    public Optional<AdminAnswerDTO> getAdminAnswerByInquiryId(Long inquiryId);

//====내 정보 ====================================================

    // 마이페이지 - 내 정보 조회
    public Optional<MemberProfileVO> getMemberProfile(Long memberId);

    // 마이페이지 - 내 정보 수정
    public void updateMemberProfile(MemberProfileVO memberProfileVO);
}



