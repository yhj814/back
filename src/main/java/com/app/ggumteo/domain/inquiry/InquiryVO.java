package com.app.ggumteo.domain.inquiry;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@Getter
@ToString
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class InquiryVO {
    @EqualsAndHashCode.Include
    private Long id;
    private String inquiryStatus;
}