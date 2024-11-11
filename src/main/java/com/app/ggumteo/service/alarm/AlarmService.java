package com.app.ggumteo.service.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;

import java.util.List;

public interface AlarmService {

    public void createApplyAuditionAlarm(Long memberProfileId, Long auditionApplicatoinId, String message);

    public List<AlarmDTO> getAlarmsByMemberId(Long memberProfileId);
}
