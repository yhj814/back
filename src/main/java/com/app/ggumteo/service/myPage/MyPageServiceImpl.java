package com.app.ggumteo.service.myPage;

import com.app.ggumteo.aspect.annotation.MyBuyFundingListLogStatus;
import com.app.ggumteo.aspect.annotation.MyFundingBuyerListLogStatus;
import com.app.ggumteo.aspect.annotation.MyFundingListLogStatus;
import com.app.ggumteo.aspect.annotation.MyInquiryHistoryListLogStatus;
import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.repository.funding.BuyFundingProductDAO;
import com.app.ggumteo.repository.funding.FundingDAO;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.repository.member.MemberDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MyPageServiceImpl implements MyPageService {
    private final MemberDAO memberDAO;
    private final FundingDAO fundingDAO;
    private final BuyFundingProductDAO buyFundingProductDAO;
    private final InquiryDAO inquiryDAO;

    @Override
    public Optional<MemberVO> getMember(Long id) {
        return memberDAO.findById(id);
    }

    //    내 펀딩 게시물 전체 조회
    @Override
    @MyFundingListLogStatus
    public MyFundingListDTO getMyFundingList(int page , WorkAndFundingPagination workAndFundingPagination, Long memberId) {
        MyFundingListDTO myFundingPosts = new MyFundingListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(fundingDAO.getTotal(memberId));
        workAndFundingPagination.progress();
        myFundingPosts.setWorkAndFundingPagination(workAndFundingPagination);
        myFundingPosts.setMyFundingPosts(fundingDAO.findByMemberId(workAndFundingPagination, memberId));

        return myFundingPosts;
    }

    //    내 펀딩 게시물 전체 개수
    @Override
    public int getMyFundingPostsTotal(Long memberId) {
        return fundingDAO.getTotal(memberId);
    }

    //    펀딩 정보 조회
    @Override
    public Optional<FundingDTO> getFunding(Long id) {
        return fundingDAO.findById(id);
    }

    //    펀딩 구매자 목록 조회
    @Override
    @MyFundingBuyerListLogStatus
    public MyFundingBuyerListDTO getMyFundingBuyerList(int page, SettingTablePagination settingTablePagination, Long fundingPostId) {
        MyFundingBuyerListDTO myFundingBuyers = new MyFundingBuyerListDTO();
        settingTablePagination.setPage(page);
        settingTablePagination.setTotal(buyFundingProductDAO.getTotal(fundingPostId));
        settingTablePagination.progress();
        myFundingBuyers.setSettingTablePagination(settingTablePagination);
        myFundingBuyers.setMyFundingBuyers(buyFundingProductDAO.findByFundingPostId(settingTablePagination, fundingPostId));

        return myFundingBuyers;
    }

    //    내 펀딩 게시물 하나의 구매자 전체 갯수
    @Override
    public int getMyFundingPostBuyersTotal(Long fundingPostId) {
        return buyFundingProductDAO.getTotal(fundingPostId);
    }

    //    펀딩상품 발송여부 체크
    @Override
    public void updateFundingSendStatus(BuyFundingProductVO buyFundingProductVO) {
        buyFundingProductDAO.updateFundingSendStatus(buyFundingProductVO);
    }

    //   내가 결제한 펀딩 목록 조회
    @Override
    @MyBuyFundingListLogStatus
    public MyBuyFundingListDTO getMyBuyFundingList(int page, WorkAndFundingPagination workAndFundingPagination, Long memberId) {
        MyBuyFundingListDTO fundingPostsPaidByMember = new MyBuyFundingListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(buyFundingProductDAO.getMyBuyFundingListTotal(memberId));
        workAndFundingPagination.progress();
        fundingPostsPaidByMember.setWorkAndFundingPagination(workAndFundingPagination);
        fundingPostsPaidByMember.setMyBuyFundingPosts(buyFundingProductDAO.findMyBuyFundingList(workAndFundingPagination, memberId));

        return fundingPostsPaidByMember;
    }
    //  내가 결제한 펀딩 목록 전체 갯수
    @Override
    public int getMyBuyFundingListTotal(Long memberId) {
        return buyFundingProductDAO.getMyBuyFundingListTotal(memberId);
    }

    // 마이페이지 - 문의 내역 조회
    @Override
    @MyInquiryHistoryListLogStatus
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(int page, WorkAndFundingPagination workAndFundingPagination, Long memberId) {
        MyInquiryHistoryListDTO myInquiryHistories = new MyInquiryHistoryListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setPage(inquiryDAO.getTotalInquiryHistoryByMember(memberId));
        workAndFundingPagination.progress();
        myInquiryHistories.setWorkAndFundingPagination(workAndFundingPagination);
        myInquiryHistories.setMyInquiryHistories(inquiryDAO.findInquiryHistoryByMember(workAndFundingPagination, memberId));

        return myInquiryHistories;
    }

    // 마이페이지 - 문의 내역 전체 갯수
    @Override
    public int getMyInquiryHistoriesTotal(Long memberId) {

        return inquiryDAO.getTotalInquiryHistoryByMember(memberId);
    }
}
