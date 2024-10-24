package com.app.ggumteo.domain.notification;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReplyNotificationVO {
    @EqualsAndHashCode.Include
    private Long id;
    private Long replyId;
}
