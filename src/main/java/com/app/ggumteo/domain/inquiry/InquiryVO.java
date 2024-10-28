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
    private Long id; // 문의 ID
    private Long memberProfileId; // 회원 프로필 ID
    private String inquiryStatus; // 문의 상태
    private String title; // 문의 제목
    private String description; // 문의 내용
}
