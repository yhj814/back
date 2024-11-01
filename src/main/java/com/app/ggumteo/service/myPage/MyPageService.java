package com.app.ggumteo.service.myPage;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingBuyerListDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;

import java.util.List;
import java.util.Optional;

public interface MyPageService {
    //    회원 정보 조회
    public Optional<MemberVO> getMember(Long id);

    //    내 펀딩 게시물 전체 조회
    public MyFundingListDTO getMyFundingList(int page , WorkAndFundingPagination workAndFundingPagination, Long memberId);

    //    내 펀딩 게시물 전체 개수
    public int getMyFundingPostsTotal(Long memberId);

    //    펀딩 정보 조회
    public Optional<FundingDTO> getFunding(Long id);

    //    펀딩 구매자 목록 조회
    public MyFundingBuyerListDTO getMyFundingBuyerList(
           int page, SettingTablePagination settingTablePagination, Long fundingPostId);

    //    내 펀딩 게시물 하나의 구매자 전체 갯수
    public int getMyFundingPostBuyerTotal(Long fundingPostId);
}
