package com.app.ggumteo.repository.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.BuyFundingProductMapper;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BuyFundingProductDAO {
    private final BuyFundingProductMapper buyFundingProductMapper;

//    펀딩 구매자 목록 조회
    public List<BuyFundingProductDTO> findByFundingPostId(SettingTablePagination settingTablePagination, Long fundingPostId) {
        return buyFundingProductMapper.selectByFundingPostId(settingTablePagination, fundingPostId);
    }

//    내 펀딩 게시물 하나의 구매자 전체 갯수
    public int getTotal(Long fundingPostId){
        return buyFundingProductMapper.selectCount(fundingPostId);
    }
}
