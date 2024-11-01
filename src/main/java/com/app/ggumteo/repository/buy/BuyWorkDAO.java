package com.app.ggumteo.repository.buy;

import com.app.ggumteo.domain.buy.BuyWorkVO;
import com.app.ggumteo.mapper.buy.BuyWorkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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


}
