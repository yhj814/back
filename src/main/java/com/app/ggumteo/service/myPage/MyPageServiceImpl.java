package com.app.ggumteo.service.myPage;

import com.app.ggumteo.aspect.annotation.*;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.controller.member.MemberRestController;
import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.audition.*;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MySettingTablePagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.repository.audition.AuditionApplicationDAO;
import com.app.ggumteo.repository.audition.AuditionDAO;
import com.app.ggumteo.repository.buy.BuyFundingProductDAO;
import com.app.ggumteo.repository.buy.BuyWorkDAO;
import com.app.ggumteo.repository.funding.FundingDAO;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.repository.member.MemberDAO;
import com.app.ggumteo.repository.member.MemberProfileDAO;
import com.app.ggumteo.repository.work.WorkDAO;
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
    private final AuditionDAO auditionDAO;
    private final AuditionApplicationDAO auditionApplicationDAO;
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
    public MyWorkListDTO getMyVideoWorkList(int page, MyWorkAndFundingPagination myWorkAndFundingPagination, Long memberId, String postType) {
        MyWorkListDTO myWorkPosts = new MyWorkListDTO();
        myWorkAndFundingPagination.setPage(page);
        myWorkAndFundingPagination.setTotal(workDAO.getTotal(memberId, PostType.WORKVIDEO.name()));
        myWorkAndFundingPagination.progress();
        myWorkPosts.setMyWorkAndFundingPagination(myWorkAndFundingPagination);
        myWorkPosts.setMyWorkPosts(workDAO.findByMemberId(myWorkAndFundingPagination, memberId, PostType.WORKVIDEO.name()));

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
    public MyWorkBuyerListDTO getMyVideoWorkBuyerList(int page, MySettingTablePagination mySettingTablePagination, Long workPostId) {
        MyWorkBuyerListDTO myWorkBuyers = new MyWorkBuyerListDTO();
        mySettingTablePagination.setPage(page);
        mySettingTablePagination.setTotal(buyWorkDAO.getTotal(workPostId));
        mySettingTablePagination.progress();
        myWorkBuyers.setMySettingTablePagination(mySettingTablePagination);
        myWorkBuyers.setMyWorkBuyers(buyWorkDAO.findByWorkPostId(mySettingTablePagination, workPostId));

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
    public MyBuyWorkListDTO getMyBuyVideoWorkList(int page, MyWorkAndFundingPagination myWorkAndFundingPagination, Long memberId, String postType) {
        MyBuyWorkListDTO myBuyWorkPosts = new MyBuyWorkListDTO();
        myWorkAndFundingPagination.setPage(page);
        myWorkAndFundingPagination.setTotal(buyWorkDAO.getMyBuyWorkListTotal(memberId, PostType.WORKVIDEO.name()));
        myWorkAndFundingPagination.progress();
        myBuyWorkPosts.setMyWorkAndFundingPagination(myWorkAndFundingPagination);
        myBuyWorkPosts.setMyBuyWorkPosts(buyWorkDAO
                .findMyBuyWorkList(myWorkAndFundingPagination, memberId, PostType.WORKVIDEO.name()));

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
    public MyFundingListDTO getMyVideoFundingList(int page , MyWorkAndFundingPagination myWorkAndFundingPagination, Long memberId, String postType) {
        MyFundingListDTO myFundingPosts = new MyFundingListDTO();
        myWorkAndFundingPagination.setPage(page);
        myWorkAndFundingPagination.setTotal(fundingDAO.getTotal(memberId, PostType.FUNDINGVIDEO.name()));
        myWorkAndFundingPagination.progress();
        myFundingPosts.setMyWorkAndFundingPagination(myWorkAndFundingPagination);
        myFundingPosts.setMyFundingPosts(fundingDAO.findByMemberId(myWorkAndFundingPagination, memberId, PostType.FUNDINGVIDEO.name()));

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
    public MyFundingBuyerListDTO getMyFundingBuyerList(int page, MySettingTablePagination mySettingTablePagination, Long fundingPostId) {
        MyFundingBuyerListDTO myFundingBuyers = new MyFundingBuyerListDTO();
        mySettingTablePagination.setPage(page);
        mySettingTablePagination.setTotal(buyFundingProductDAO.getTotal(fundingPostId));
        mySettingTablePagination.progress();
        myFundingBuyers.setMySettingTablePagination(mySettingTablePagination);
        myFundingBuyers.setMyFundingBuyers(buyFundingProductDAO.findByFundingPostId(mySettingTablePagination, fundingPostId));

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
    public MyBuyFundingListDTO getMyBuyFundingList(int page, MyWorkAndFundingPagination myWorkAndFundingPagination
            , Long memberId, String postType) {
        MyBuyFundingListDTO fundingPostsPaidByMember = new MyBuyFundingListDTO();
        myWorkAndFundingPagination.setPage(page);
        myWorkAndFundingPagination.setTotal(buyFundingProductDAO.getMyBuyFundingListTotal(memberId, PostType.FUNDINGVIDEO.name()));
        myWorkAndFundingPagination.progress();
        fundingPostsPaidByMember.setMyWorkAndFundingPagination(myWorkAndFundingPagination);
        fundingPostsPaidByMember.setMyBuyFundingPosts(buyFundingProductDAO
                .findMyBuyFundingList(myWorkAndFundingPagination, memberId, PostType.FUNDINGVIDEO.name()));

        return fundingPostsPaidByMember;
    }
    //    내가 결제한 펀딩 목록 전체 갯수
    @Override
    public int getMyBuyFundingListTotal(Long memberId, String postType) {
        return buyFundingProductDAO.getMyBuyFundingListTotal(memberId, postType);
    }

//************************************************************************************************

    //    나의 모집 게시물 전체 목록 - 영상
    @Override
    public MyAuditionListDTO getMyVideoAuditionList(int page, MyAuditionPagination myAuditionPagination, Long memberId, String postType) {
        MyAuditionListDTO myAuditionPosts = new MyAuditionListDTO();
        myAuditionPagination.setPage(page);
        myAuditionPagination.setTotal(auditionDAO.getTotal(memberId, PostType.AUDITIONVIDEO.name()));
        myAuditionPagination.progress();
        myAuditionPosts.setMyAuditionPagination(myAuditionPagination);
        myAuditionPosts.setMyAuditionPosts(auditionDAO.findByMemberId(myAuditionPagination, memberId, PostType.AUDITIONVIDEO.name()));

        return myAuditionPosts;
    }

    //    나의 모집 게시물 전체 개수
    @Override
    public int getMyVideoAuditionPostsTotal(Long memberId, String postType) {
        return auditionDAO.getTotal(memberId, postType);
    }

    //    모집 정보 조회
    @Override
    public Optional<AuditionDTO> getAudition(Long id, String postType) {
        return auditionDAO.findByIdAndPostType(id, postType);
    }

    // 나의 모집 지원자 목록 조회
    @Override
    @MyAuditionApplicantListLogStatus
    public MyAuditionApplicantListDTO getMyVideoAuditionApplicantList(int page, MySettingTablePagination mySettingTablePagination, Long auditionId) {
        MyAuditionApplicantListDTO myAuditionApplicants = new MyAuditionApplicantListDTO();
        mySettingTablePagination.setPage(page);
        mySettingTablePagination.setTotal(auditionApplicationDAO.getTotal(auditionId));
        mySettingTablePagination.progress();
        myAuditionApplicants.setMySettingTablePagination(mySettingTablePagination);
        myAuditionApplicants.setMyAuditionApplicants(auditionApplicationDAO.findByAuditionPostId(mySettingTablePagination, auditionId));

        return myAuditionApplicants;
    }

    // 나의 모집 게시글 한개당 지원자 전체 갯수
    @Override
    public int getMyVideoAuditionApplicantsTotal(Long auditionId) {
        return auditionApplicationDAO.getTotal(auditionId);
    }

    // 확인 여부
    @Override
    public void updateConfirmStatus(AuditionApplicationVO auditionApplicationVO) {
        auditionApplicationDAO.updateConfirmStatus(auditionApplicationVO);
    }

    // 내가 신청한 모집 목록 조회
    @Override
    public MyApplicationAuditionListDTO getMyVideoApplicationAuditionList(int page, MyAuditionPagination myAuditionPagination, Long memberId, String postType) {
        MyApplicationAuditionListDTO myApplicationAuditionPosts = new MyApplicationAuditionListDTO();
        myAuditionPagination.setPage(page);
        myAuditionPagination.setTotal(auditionApplicationDAO.getMyAuditionApplicationListTotal(memberId, PostType.AUDITIONVIDEO.name()));
        myAuditionPagination.progress();
        myApplicationAuditionPosts.setMyAuditionPagination(myAuditionPagination);
        myApplicationAuditionPosts.setMyApplicationAuditionPosts(auditionApplicationDAO
                .findMyAppliedAuditionList(myAuditionPagination, memberId, PostType.AUDITIONVIDEO.name()));

        return myApplicationAuditionPosts;
    }

    // 내가 신청한 모집 전체 갯수
    @Override
    public int getMyApplicationAuditionListTotal(Long memberId, String postType) {
        return auditionApplicationDAO.getMyAuditionApplicationListTotal(memberId, postType);
    }

    //    마이페이지 - 문의 내역 조회
    @Override
    @MyInquiryHistoryListLogStatus
    public MyInquiryHistoryListDTO getMyInquiryHistoryList(int page, MyWorkAndFundingPagination myWorkAndFundingPagination, Long memberId) {
        MyInquiryHistoryListDTO myInquiryHistories = new MyInquiryHistoryListDTO();
        myWorkAndFundingPagination.setPage(page);
        myWorkAndFundingPagination.setTotal(inquiryDAO.getTotalInquiryHistoryByMember(memberId));
        myWorkAndFundingPagination.progress();
        myInquiryHistories.setMyWorkAndFundingPagination(myWorkAndFundingPagination);
        myInquiryHistories.setMyInquiryHistories(inquiryDAO.findInquiryHistoryByMember(myWorkAndFundingPagination, memberId));

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
