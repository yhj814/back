package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FundingMapper {
//    내 펀딩 게시물 전체 조회
    public List<FundingDTO> selectByMemberId(@Param("workAndFundingPagination") WorkAndFundingPagination workAndFundingPagination
            , @Param("memberId") Long memberId);

//    내 펀딩 게시물 전체 갯수
    public int selectCount(Long memberId);

//    펀딩 정보 조회
    public Optional<FundingDTO> selectById(Long id);
}
