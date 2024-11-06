package com.app.ggumteo.domain.buy;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BuyFundingProductDTO {
    private Long id; // 펀딩상품 구매 ID
    private Long memberProfileId; // 회원 프로필 ID
    private Long fundingProductId; // 펀딩 상품 ID
    private String fundingSendStatus; // 발송 여부
    private String productName; // 상품 이름
    private String productPrice; // 상품 가격
    private Long fundingId; // 펀딩 ID
    private String profileName; // 구매자 이름
    private String profileEmail; // 구매자 이메일
    private Long memberId;
    private String postTitle;
    private String postContent;
    private String genreType;
    private String createdDate;
    private String profileImgUrl;
    private String profileNickname;
    private String fileName; // 파일명
    private String fileSize; // 파일 사이즈
    private String fileType; // 파일 타입
    private String filePath; // 파일 경로

    public BuyFundingProductVO toVO() {
        return new BuyFundingProductVO(id, memberProfileId, fundingProductId, fundingSendStatus, createdDate);
    }
}
