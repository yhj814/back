package com.app.ggumteo.domain.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO {
    private Long id;
    private String inquiryStatus;
}


