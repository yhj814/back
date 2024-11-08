package com.app.ggumteo.repository.buy;

import com.app.ggumteo.domain.buy.BuyFundingProductDTO;
import com.app.ggumteo.domain.buy.BuyWorkDTO;
import com.app.ggumteo.domain.buy.BuyWorkVO;
import com.app.ggumteo.mapper.buy.BuyWorkMapper;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BuyWorkDAO {

    private final BuyWorkMapper buyWorkMapper;

    public void savePurchase(BuyWorkVO buyWorkVO) {
        try {
            buyWorkMapper.insertPurchase(buyWorkVO);
        } catch (Exception e) {
            log.error("Insert 작업 중 오류 발생: ", e);
            throw e;
        }
    }

    //    작품 구매자 목록 조회
    public List<BuyWorkDTO> findByWorkPostId(SettingTablePagination settingTablePagination , Long workPostId) {
        return buyWorkMapper.selectByWorkPostId(settingTablePagination, workPostId);
    };

    //    내 작품 게시물 하나의 구매자 전체 갯수
    public int getTotal(Long workPostId) {
        return buyWorkMapper.selectCount(workPostId);
    };

    //    발송 여부 체크
    public void updateWorkSendStatus(BuyWorkVO buyWorkVO) {
        buyWorkMapper.updateWorkSendStatus(buyWorkVO);
    };

    //   내가 구매한 작품 목록 조회
    public List<BuyWorkDTO> findMyBuyWorkList(WorkAndFundingPagination workAndFundingPagination
            , Long memberId, String postType) {
        return buyWorkMapper.selectBuyWorkListByMember(workAndFundingPagination, memberId, postType);
    }

    //  내가 구매한 작품 목록 전체 갯수
    public int getMyBuyWorkListTotal(Long memberId, String postType){
        return buyWorkMapper.selectCountBuyWorkListByMember(memberId, postType);
    }

    // 내가 구매한 작품 결제 내역 삭제
    public void deleteBuyWorkPost(Long id) {
        buyWorkMapper.deleteBuyWorkPost(id);
    }

}
