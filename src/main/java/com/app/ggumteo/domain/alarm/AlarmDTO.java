package com.app.ggumteo.domain.alarm;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private Long id;
    private Long memberProfileId;
    private Long dataId;
    private String message;
    private boolean isRead;
    private String alarmType;
    private String createDate;
}
