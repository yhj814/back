package com.app.ggumteo.domain.audition;

import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyAuditionListDTO {
    private List<AuditionDTO> myAuditionPosts;
    private MyAuditionPagination myAuditionPagination;
}
