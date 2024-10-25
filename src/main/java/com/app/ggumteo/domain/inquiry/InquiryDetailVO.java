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
public class InquiryDetailVO {
    private Long postId; // 문의 ID
    private String createdDate; // 생성 날짜
    private String postTitle; // 제목
    private String postContent; // 내용
    private String memberName; // 회원 이름
    private String memberEmail; // 회원 이메일
    private String inquiryStatus; // 문의 상태
    private String adminAnswerDate; // 답변 날짜
}

