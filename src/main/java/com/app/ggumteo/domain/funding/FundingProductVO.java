package com.app.ggumteo.domain.funding;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FundingProductVO {
    private Long id; // 펀딩 상품 ID
    private String productName; // 상품명
    private int productPrice; // 상품 가격
    private int productAmount; // 상품 수량
    private Long fundingId; // 펀딩 ID와 연결
}
