package com.app.ggumteo.domain.work;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyWorkListDTO {
    private List<WorkDTO> myWorkPosts;
    private WorkAndFundingPagination workAndFundingPagination;
}
