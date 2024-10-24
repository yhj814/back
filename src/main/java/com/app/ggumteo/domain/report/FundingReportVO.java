package com.app.ggumteo.domain.report;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class FundingReportVO {
    @EqualsAndHashCode.Include
    private Long id;
    private String reportContent;
    private Long fundingId;
    private String createdDate;
}
