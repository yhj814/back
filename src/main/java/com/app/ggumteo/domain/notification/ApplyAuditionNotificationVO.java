package com.app.ggumteo.domain.notification;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApplyAuditionNotificationVO {
    @EqualsAndHashCode.Include
    private Long id;
    private Long auditionApplicationId;
}
