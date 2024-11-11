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
    private String message;
    private String createDate;
    private Boolean isRead;

    public AlarmVO toVO() {return new AlarmVO(id, memberProfileId, message, createDate, isRead);}
}
