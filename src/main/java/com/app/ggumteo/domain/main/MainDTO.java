package com.app.ggumteo.domain.main;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MainDTO {
    private int memberTotalCount;
    private int workTotalCount;
    private int fundingTotalMoney;
    private double FundingPercentage;
}
