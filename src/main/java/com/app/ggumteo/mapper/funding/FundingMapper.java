package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundingMapper {
//    내 펀딩 게시물 조회
    public List<FundingDTO> selectByMemberId(@Param("myPagePagination")MyPagePagination myPagePagination, @Param("memberId") Long memberId);

//    내 펀딩 게시물 전체 갯수
    public int selectCount(Long memberId);

//    구매자 목록 조회
    public List<BuyFundingProductDTO> selectBuyerByMemberId(Long fundingPostId);
}
