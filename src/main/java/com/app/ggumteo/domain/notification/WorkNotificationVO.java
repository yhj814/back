package com.app.ggumteo.domain.notification;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkNotificationVO {
    @EqualsAndHashCode.Include
    private Long id;
    private Long memberId;
    private Long workId;
}
