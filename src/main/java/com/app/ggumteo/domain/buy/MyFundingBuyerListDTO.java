package com.app.ggumteo.domain.buy;

import com.app.ggumteo.pagination.SettingTablePagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyFundingBuyerListDTO {
    private List<BuyFundingProductDTO> myFundingBuyers;
    private SettingTablePagination settingTablePagination;
}
