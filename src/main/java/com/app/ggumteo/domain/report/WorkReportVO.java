package com.app.ggumteo.domain.report;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkReportVO {
    @EqualsAndHashCode.Include
    private Long id;
    private String reportContent;
    private Long workId;
    private String createdDate;
}
