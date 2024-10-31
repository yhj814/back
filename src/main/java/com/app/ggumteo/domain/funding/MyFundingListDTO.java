package com.app.ggumteo.domain.funding;

import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyFundingListDTO {
    private List<FundingDTO> myFundingPosts;
    private WorkAndFundingPagination workAndFundingPagination;
}
