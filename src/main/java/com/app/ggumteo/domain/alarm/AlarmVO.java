package com.app.ggumteo.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlarmVO {
    private Long id;
    private Long memberProfileId;
    private Long dataId;
    private String message;
    private boolean isRead;
    private String alarmType;
    private String createDate;
}
