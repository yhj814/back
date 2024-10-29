package com.app.ggumteo.domain.funding;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BuyFundingProductDTO {
    private Long id; // 펀딩 ID
    private Long memberProfileId; // 회원 프로필 ID
    private Long fundingProductId; // 펀딩 상품 ID
    private String fundingSendStatus; // 발송 여부
    private String productName; // 상품 이름
    private String productPrice; // 상품 가격
    private String profileName; // 구매자 이름
    private String profileEmail; // 구매자 이메일

    public BuyFundingProductVO toVO() {
        return new BuyFundingProductVO(id, memberProfileId, fundingProductId, fundingSendStatus);
    }
}