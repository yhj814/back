package com.app.ggumteo.domain.buy;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BuyWorkVO {
    private Long id;
    private Long workId;
    private String workSendStatus;
    private Long memberProfileId;
    private String createdDate;
}
