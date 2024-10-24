package com.app.ggumteo.domain.funding;

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
public class FundingVO {

    private Long id; // 펀딩 ID
    private int investorNumber; // 투자자 수
    private int targetPrice; // 목표 금액
    private int convergePrice; // 모인 금액
    private Long genreId; // 장르 ID
    private String fundingStatus; // 펀딩 상태
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜

}
