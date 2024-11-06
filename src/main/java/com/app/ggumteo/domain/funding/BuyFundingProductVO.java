package com.app.ggumteo.domain.funding;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BuyFundingProductVO {
    @EqualsAndHashCode.Include
    private Long id; // 펀딩 ID
    private Long memberProfileId; // 회원 프로필 ID
    private Long fundingProductId; // 펀딩 상품 ID
    private String fundingSendStatus; // 발송 여부
    private String createdDate; // 구매 날짜
}
