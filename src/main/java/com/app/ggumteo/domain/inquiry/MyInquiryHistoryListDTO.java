package com.app.ggumteo.domain.inquiry;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter @Setter @ToString
public class MyInquiryHistoryListDTO {
    private List<InquiryDTO> myInquiryHistories;
    private WorkAndFundingPagination workAndFundingPagination;
}

