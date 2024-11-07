package com.app.ggumteo.repository.buy;

import com.app.ggumteo.domain.buy.BuyFundingProductDTO;
import com.app.ggumteo.domain.buy.BuyFundingProductVO;
import com.app.ggumteo.mapper.buy.BuyFundingProductMapper;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

//    발송 여부 체크
    public void updateFundingSendStatus(BuyFundingProductVO buyFundingProductVO) {
        buyFundingProductMapper.updateFundingSendStatus(buyFundingProductVO);
    }

//   결제한 펀딩 목록 조회
    public List<BuyFundingProductDTO> findMyBuyFundingList(WorkAndFundingPagination workAndFundingPagination
            ,Long memberId, String postType) {
        return buyFundingProductMapper.selectBuyFundingListByMember(workAndFundingPagination, memberId, postType);
    }

//  내가 결제한 펀딩 목록 전체 갯수
    public int getMyBuyFundingListTotal(Long memberId, String postType){
        return buyFundingProductMapper.selectCountBuyFundingListByMember(memberId, postType);
    }

    // 펀딩 금액 업데이트
    public void updateConvergePrice(Long fundingId, int amount) {
        buyFundingProductMapper.updateConvergePrice(fundingId, amount);
    }

    // 상품 수량 감소
    public void decrementProductAmount(Long productId) {
        buyFundingProductMapper.decrementProductAmount(productId);
    }

    // 구매 정보 삽입
    public void insertBuyFundingProduct(Long memberId, Long productId) {
        buyFundingProductMapper.insertBuyFundingProduct(memberId, productId);
    }

}
