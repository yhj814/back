package com.app.ggumteo.domain.funding;

import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyBuyFundingListDTO {
    private List<BuyFundingProductDTO> myBuyFundingPosts;
    private WorkAndFundingPagination workAndFundingPagination;
}
