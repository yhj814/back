package com.app.ggumteo.service.myPage;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.MyFundingListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.funding.FundingDAO;
import com.app.ggumteo.repository.member.MemberDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MyPageServiceImpl implements MyPageService {
    private final MemberDAO memberDAO;
    private final FundingDAO fundingDAO;


    @Override
    public Optional<MemberVO> getMember(Long Id) {
        return memberDAO.findById(Id);
    }

    @Override
    public MyFundingListDTO getMyFundingList(int page ,MyPagePagination myPagePagination, Long memberId) {
        MyFundingListDTO myFundingListDTO = new MyFundingListDTO();
        myPagePagination.setPage(page);
        myPagePagination.setTotal(fundingDAO.getTotal(memberId));
        myPagePagination.progress();
        myFundingListDTO.setMyPagePagination(myPagePagination);
        myFundingListDTO.setMyFundingPosts(fundingDAO.findByMemberId(myPagePagination, memberId));

        return myFundingListDTO;
    }

    @Override
    public int getTotal(Long memberId) {
        return fundingDAO.getTotal(memberId);
    }

    @Override
    public List<BuyFundingProductDTO> getFundingBuyerList(Long fundingPostId) {
        return fundingDAO.findBuyerByFundingPostId(fundingPostId);
    }
}
