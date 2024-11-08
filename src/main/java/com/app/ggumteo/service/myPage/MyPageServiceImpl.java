package com.app.ggumteo.service.myPage;

import com.app.ggumteo.aspect.annotation.*;
import com.app.ggumteo.constant.PostType;
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
import com.app.ggumteo.repository.buy.BuyFundingProductDAO;
import com.app.ggumteo.repository.buy.BuyWorkDAO;
import com.app.ggumteo.repository.funding.FundingDAO;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.repository.member.MemberDAO;
import com.app.ggumteo.repository.member.MemberProfileDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import com.app.ggumteo.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
@Primary
public class MyPageServiceImpl implements MyPageService {
    private final MemberDAO memberDAO;
    private final WorkDAO workDAO;
    private final BuyWorkDAO buyWorkDAO;
    private final FundingDAO fundingDAO;
    private final BuyFundingProductDAO buyFundingProductDAO;
    private final InquiryDAO inquiryDAO;
    private final MemberProfileDAO memberProfileDAO;

    //    회원 정보 조회
    @Override
    public Optional<MemberVO> getMember(Long id) {
        return memberDAO.findById(id);
    }

    //    내 작품 게시물 전체 조회 - 영상
    @Override
    @MyWorkListLogStatus
    public MyWorkListDTO getMyVideoWorkList(int page, WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType) {
        MyWorkListDTO myWorkPosts = new MyWorkListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(workDAO.getTotal(memberId, PostType.WORKVIDEO.name()));
        workAndFundingPagination.progress();
        myWorkPosts.setWorkAndFundingPagination(workAndFundingPagination);
        myWorkPosts.setMyWorkPosts(workDAO.findByMemberId(workAndFundingPagination, memberId, PostType.WORKVIDEO.name()));

        return myWorkPosts;
    }

    //    내 작품 게시물 전체 개수
    @Override
    public int getMyVideoWorkPostsTotal(Long memberId, String postType) {
        return workDAO.getTotal(memberId, postType);
    }

    //    작품 정보 조회
    @Override
    public Optional<WorkDTO> getWork(Long id, String postType) {
        return workDAO.findByIdAndPostType(id, postType);
    }


    //    작품 구매자 목록 조회
    @Override
    @MyWorkBuyerListLogStatus
    public MyWorkBuyerListDTO getMyVideoWorkBuyerList(int page, SettingTablePagination settingTablePagination, Long workPostId) {
        MyWorkBuyerListDTO myWorkBuyers = new MyWorkBuyerListDTO();
        settingTablePagination.setPage(page);
        settingTablePagination.setTotal(buyWorkDAO.getTotal(workPostId));
        settingTablePagination.progress();
        myWorkBuyers.setSettingTablePagination(settingTablePagination);
        myWorkBuyers.setMyWorkBuyers(buyWorkDAO.findByWorkPostId(settingTablePagination, workPostId));

        return myWorkBuyers;
    }

    //    내 작품 게시물 하나의 구매자 전체 갯수
    @Override
    public int getMyVideoWorkBuyersTotal(Long workPostId) {
        return buyWorkDAO.getTotal(workPostId);
    }

    //    작품 발송 여부 체크
    @Override
    public void updateWorkSendStatus(BuyWorkVO buyWorkVO) {
        buyWorkDAO.updateWorkSendStatus(buyWorkVO);
    }

    //    내가 구매한 작품 목록 조회 - 영상
    @Override
    public MyBuyWorkListDTO getMyBuyVideoWorkList(int page, WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType) {
        MyBuyWorkListDTO myBuyWorkPosts = new MyBuyWorkListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(buyWorkDAO.getMyBuyWorkListTotal(memberId, PostType.WORKVIDEO.name()));
        workAndFundingPagination.progress();
        myBuyWorkPosts.setWorkAndFundingPagination(workAndFundingPagination);
        myBuyWorkPosts.setMyBuyWorkPosts(buyWorkDAO
                .findMyBuyWorkList(workAndFundingPagination, memberId, PostType.WORKVIDEO.name()));

        return myBuyWorkPosts;
    }

    //    내가 구매한 작품 목록 전체 갯수
    @Override
    public int getMyBuyWorkListTotal(Long memberId, String postType) {
        return buyWorkDAO.getMyBuyWorkListTotal(memberId, postType);
    }

    //    내가 구매한 작품 결제 내역 삭제
    @Override
    public void deleteBuyWorkPost(Long id) {
        buyWorkDAO.deleteBuyWorkPost(id);
    }

    //    내 펀딩 게시물 전체 조회 - 영상
    @Override
    @MyFundingListLogStatus
    public MyFundingListDTO getMyVideoFundingList(int page , WorkAndFundingPagination workAndFundingPagination, Long memberId, String postType) {
        MyFundingListDTO myFundingPosts = new MyFundingListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(fundingDAO.getTotal(memberId, PostType.FUNDINGVIDEO.name()));
        workAndFundingPagination.progress();
        myFundingPosts.setWorkAndFundingPagination(workAndFundingPagination);
        myFundingPosts.setMyFundingPosts(fundingDAO.findByMemberId(workAndFundingPagination, memberId, PostType.FUNDINGVIDEO.name()));

        return myFundingPosts;
    }

    //    내 펀딩 게시물 전체 개수
    @Override
    public int getMyFundingPostsTotal(Long memberId, String postType) {
        return fundingDAO.getTotal(memberId, postType);
    }

    //    펀딩 정보 조회
    @Override
    public Optional<FundingDTO> getFunding(Long id, String postType) {
        return fundingDAO.findById(id, postType);
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

    //    내가 결제한 펀딩 목록 조회 - 영상
    @Override
    @MyBuyFundingListLogStatus
    public MyBuyFundingListDTO getMyBuyFundingList(int page, WorkAndFundingPagination workAndFundingPagination
            , Long memberId, String postType) {
        MyBuyFundingListDTO fundingPostsPaidByMember = new MyBuyFundingListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(buyFundingProductDAO.getMyBuyFundingListTotal(memberId, PostType.FUNDINGVIDEO.name()));
        workAndFundingPagination.progress();
        fundingPostsPaidByMember.setWorkAndFundingPagination(workAndFundingPagination);
        fundingPostsPaidByMember.setMyBuyFundingPosts(buyFundingProductDAO
                .findMyBuyFundingList(workAndFundingPagination, memberId, PostType.FUNDINGVIDEO.name()));

        return fundingPostsPaidByMember;
    }
    //    내가 결제한 펀딩 목록 전체 갯수
    @Override
    public int getMyBuyFundingListTotal(Long memberId, String postType) {
        return buyFundingProductDAO.getMyBuyFundingListTotal(memberId, postType);
    }

    //    마이페이지 - 문의 내역 조회
    @Override
    @MyInquiryHistoryListLogStatus
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(int page, WorkAndFundingPagination workAndFundingPagination, Long memberId) {
        MyInquiryHistoryListDTO myInquiryHistories = new MyInquiryHistoryListDTO();
        workAndFundingPagination.setPage(page);
        workAndFundingPagination.setTotal(inquiryDAO.getTotalInquiryHistoryByMember(memberId));
        workAndFundingPagination.progress();
        myInquiryHistories.setWorkAndFundingPagination(workAndFundingPagination);
        myInquiryHistories.setMyInquiryHistories(inquiryDAO.findInquiryHistoryByMember(workAndFundingPagination, memberId));

        return myInquiryHistories;
    }

    //    마이페이지 - 문의 내역 전체 갯수
    @Override
    public int getMyInquiryHistoriesTotal(Long memberId) {
        return inquiryDAO.getTotalInquiryHistoryByMember(memberId);
    }

    //    문의 정보 조회
    @Override
    public Optional<InquiryDTO> getInquiry(Long postId) {
        return inquiryDAO.findById(postId);
    }

    //    마이페이지 - 내 문의 관리자 답변
    @Override
    public Optional<AdminAnswerDTO> getAdminAnswerByInquiryId(Long inquiryId) {
        return inquiryDAO.findAdminAnswerByInquiryId(inquiryId);
    }

    //    마이페이지 - 내 정보 조회
    @Override
    public Optional<MemberProfileVO> getMemberProfile(Long memberId) {
        return memberProfileDAO.findByMemberId(memberId);
    }

    //    마이페이지 - 내 정보 수정
    @Override
    public void updateMemberProfile(MemberProfileVO memberProfileVO) {
        memberProfileDAO.setMemberProfile(memberProfileVO);
    }
}
