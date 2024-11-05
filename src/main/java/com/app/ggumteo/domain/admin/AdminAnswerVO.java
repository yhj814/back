package com.app.ggumteo.domain.admin;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminAnswerVO {
    private Long id;
    private String adminAnswerContent;
    private Long inquiryId;
    private String createdDate;
}
