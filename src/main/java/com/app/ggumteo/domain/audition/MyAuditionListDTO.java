package com.app.ggumteo.domain.audition;

import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyAuditionListDTO {
    private List<AuditionDTO> myAuditionPosts;
    private WorkAndFundingPagination workAndFundingPagination;
}
