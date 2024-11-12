package com.app.ggumteo.domain.inquiry;

import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter @Setter @ToString
public class MyInquiryHistoryListDTO {
    private List<InquiryDTO> myInquiryHistories;
    private MyWorkAndFundingPagination myWorkAndFundingPagination;
}

