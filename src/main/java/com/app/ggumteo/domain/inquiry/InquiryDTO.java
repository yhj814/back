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
    private Long memberProfileId; // 회원 프로필 ID
    private String inquiryStatus; // 문의 상태
    private String title; // 문의 제목
    private String description; // 문의 내용
}


