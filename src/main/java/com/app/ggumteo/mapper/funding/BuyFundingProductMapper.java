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
//    구매자 목록 조회
    public List<BuyFundingProductDTO> selectBuyerByFundingPostId(
            @Param("settingTablePagination") SettingTablePagination settingTablePagination ,Long fundingPostId);
}
