package com.app.ggumteo.domain.funding;

import com.app.ggumteo.pagination.MyPagePagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyFundingListDTO {
    private List<FundingDTO> fundingPosts;
    private MyPagePagination myPagePagination;
}
