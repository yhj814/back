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
    private String admin_answer_content;
    private Long inquiryId;
    private String created_date;
}
