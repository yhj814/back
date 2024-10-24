package com.app.ggumteo.domain.funding;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FundingDTO {
    private Long id; // 펀딩 ID
    private int investorNumber; // 투자자 수
    private int targetPrice; // 목표 금액
    private int convergePrice; // 모인 금액
    private Long genreId; // 장르 ID
    private String genreType; // 장르 타입
    private String postTitle; // 포스트 제목
    private String postContent; // 포스트 내용
    private String postType; // 포스트 타입 (영상, 글, 문의사항 등)
    private Long memberId; // 회원 ID
    private String fundingStatus; // 펀딩 상태
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
    private String memberName; // 멤버 이름


}
