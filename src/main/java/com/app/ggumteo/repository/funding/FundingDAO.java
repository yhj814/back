package com.app.ggumteo.repository.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

//    펀딩 정보 조회
    public Optional<FundingDTO> findById(Long id) {
        return fundingMapper.selectById(id);
    };

//    구매자 목록 조회
    public List<BuyFundingProductDTO> findBuyerByFundingPostId(SettingTablePagination settingTablePagination, Long fundingPostId) {
        return fundingMapper.selectBuyerByFundingPostId(settingTablePagination, fundingPostId);
    }
}
