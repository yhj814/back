package com.app.ggumteo.domain.buy;

import com.app.ggumteo.pagination.MySettingTablePagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyWorkBuyerListDTO {
    private List<BuyWorkDTO> myWorkBuyers;
    private MySettingTablePagination mySettingTablePagination;
}
