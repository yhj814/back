package com.app.ggumteo.domain.inquiry;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class InquiryVO {
    @EqualsAndHashCode.Include
    private Long id; // 문의 ID
    private String inquiryStatus; // 문의 상태 : 0 미답변 1 답변완료
    private Long postId; // 게시물 ID
}
