package com.app.ggumteo.domain.work;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyWorkBuyerListDTO {
    private List<BuyWorkDTO> myWorkBuyers;
    private SettingTablePagination settingTablePagination;
}
