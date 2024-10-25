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
public class AdminAnswerDTO {
    private Long id;
    private String admin_answer_content;
    private Long inquiryId;
    private String created_date;

    public AdminAnswerVO toVO() {
        return new AdminAnswerVO(id, admin_answer_content, inquiryId, created_date);
    }
}
