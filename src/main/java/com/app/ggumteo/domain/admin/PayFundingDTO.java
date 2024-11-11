package com.app.ggumteo.domain.admin;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PayFundingDTO {
    @EqualsAndHashCode.Include
    private Long Id;                    // tbl_funding_product 의 id
    private String postType;            // post_type에 FUNDINGVIDEO 붙으면 영상, FUNDINGTEXT 붙으면 글으로 구분
    private String productName;         // tbl_funding_product product_name 상품명
    private String productPrice;        // 작품 가격
    private String profileName;         // tbl_buy_funding_product 의 member_profile_id ,구매자
    private String createdDate;         // tbl_buy_funding_product 의 created_date
}
