package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BuyFundingProductMapper {
//    펀딩 구매자 목록 조회
    public List<BuyFundingProductDTO> selectByFundingPostId(
            @Param("settingTablePagination") SettingTablePagination settingTablePagination ,Long fundingPostId);

//    내 펀딩 게시물 하나의 구매자 전체 갯수
    public int selectCount(Long fundingPostId);
}
