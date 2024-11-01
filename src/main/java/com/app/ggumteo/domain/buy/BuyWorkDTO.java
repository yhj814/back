package com.app.ggumteo.domain.buy;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class BuyWorkDTO {
    private Long id;
    private Long workId;
    private String workSendStatus;
    private Long memberProfileId;
    private String profileName;
    private String profilePhone;
    private String profileEmail;
    private String createdDate;

    public BuyWorkVO toVO(){
        return new BuyWorkVO(id, workId, workSendStatus, memberProfileId, createdDate);
    }
}
