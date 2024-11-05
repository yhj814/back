package com.app.ggumteo.domain.funding;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FundingVO {
    @EqualsAndHashCode.Include
    private Long id; // 펀딩 ID
    private String genreType;
    private int investorNumber; // 투자자 수
    private int targetPrice; // 목표 금액
    private int convergePrice; // 모인 금액
    private String fileContent; // 파일 설명
    private String fundingStatus; // 펀딩 상태
    private String fundingContent;
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
}
