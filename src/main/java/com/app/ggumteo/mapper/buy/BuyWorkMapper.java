package com.app.ggumteo.mapper.buy;

import com.app.ggumteo.domain.buy.BuyWorkVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuyWorkMapper {
    public void insertPurchase(BuyWorkVO buyWorkVO);

}
