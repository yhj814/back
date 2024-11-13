package com.app.ggumteo.domain.notification;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApplyAuditionNotificationVO {
    @EqualsAndHashCode.Include
    private Long id;
    private Long member_profile_id;
    private Long auditionApplicationId;
    private String message;
    private boolean isRead;
    private String createdDate;
}
