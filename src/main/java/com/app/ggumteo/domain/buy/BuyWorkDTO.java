package com.app.ggumteo.domain.buy;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class BuyWorkDTO {
    private Long id; // 작품 ID
    private Long workId; // 작품 상품 ID
    private String workSendStatus; // 발송 여부
    private Long memberProfileId; // 회원 프로필 ID
    private String profileName;
    private String profilePhone;
    private String profileEmail;
    private String createdDate; // 구매 날짜
    private String workPrice; // 작품 가격

    public BuyWorkVO toVO(){
        return new BuyWorkVO(id, workId, workSendStatus, memberProfileId, createdDate);
    }
}
