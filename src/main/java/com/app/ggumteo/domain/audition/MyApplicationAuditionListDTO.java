package com.app.ggumteo.domain.audition;

import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyApplicationAuditionListDTO {
    private List<AuditionApplicationDTO> myApplicationAuditionPosts;
    private MyAuditionPagination myAuditionPagination;
}
