package com.app.ggumteo.service.myPage;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;

import java.util.List;
import java.util.Optional;

public interface MyPageService {
    //    회원 정보 조회
    public Optional<MemberVO> getMember(Long Id);

    //    내 펀딩 게시물 조회
    public MyFundingListDTO getMyFundingList(int page , WorkAndFundingPagination workAndFundingPagination, Long memberId);

    //    내 펀딩 게시물 전체 개수
    public int getTotal(Long memberId);

    //    구매자 목록 조회
    public List<BuyFundingProductDTO> getFundingBuyerList(Long fundingPostId);
}
