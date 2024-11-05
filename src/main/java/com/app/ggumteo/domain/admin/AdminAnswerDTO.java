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
    private String adminAnswerContent;
    private Long inquiryId;
    private String createdDate;
    private String inquiryStatus;

    public AdminAnswerVO toVO() {
        return new AdminAnswerVO(id
                , adminAnswerContent, inquiryId, createdDate);
    }
}
