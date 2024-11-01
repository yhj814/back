package com.app.ggumteo.service.buy;


import com.app.ggumteo.domain.buy.BuyWorkVO;
import com.app.ggumteo.repository.buy.BuyWorkDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyWorkServiceImpl implements BuyWorkService {

    private final BuyWorkDAO buyWorkDAO;

    @Override
    public void savePurchase(BuyWorkVO buyWorkVO) {
        buyWorkDAO.savePurchase(buyWorkVO);
    }
}

