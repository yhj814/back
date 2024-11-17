package com.app.ggumteo.domain.alarm;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private Long id;  // 알림 ID
    private String message;  // 알림 내용
    private Long memberProfileId;  // 회원 프로필 ID
    private String createdDate;  // 생성 날짜
    private Boolean isRead;  // 읽음 여부
    private Long auditionApplicationId;  // 오디션 신청 ID (영상/글 구분)
    private Long replyId;  // 댓글 ID
    private Long buyWorkId;  // 작품 구매 ID
    private Long buyFundingProductId;  // 펀딩 상품 구매 ID
    private String alarmType;  // 알림 타입 ('work', 'audition', 'reply', 'fundingProduct')
    private String postTitle;  // 게시글 제목
    private String subType;  // 영상, 글 구분
}
