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
    private String message;
    private String createdDate;
    private Boolean isRead;


    public AlarmDTO toDTO() {
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setId(id);
        alarmDTO.setMemberProfileId(memberProfileId);
        alarmDTO.setMessage(message);
        alarmDTO.setIsRead(isRead);
        alarmDTO.setCreateDate(createdDate);
        return alarmDTO;
    }
}
