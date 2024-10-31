package com.app.ggumteo.repository.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FundingDAO {
    private final FundingMapper fundingMapper;

//    내 펀딩 게시물 조회
    public List<FundingDTO> findByMemberId(WorkAndFundingPagination workAndFundingPagination, Long memberId) {
        return fundingMapper.selectByMemberId(workAndFundingPagination, memberId);
    }

//    내 펀딩 게시물 전체 개수
    public int getTotal(Long memberId){
        return fundingMapper.selectCount(memberId);
    }


//    구매자 목록 조회
    public List<BuyFundingProductDTO> findBuyerByFundingPostId(Long fundingPostId) {
        return fundingMapper.selectBuyerByFundingPostId(fundingPostId);
    }
}
