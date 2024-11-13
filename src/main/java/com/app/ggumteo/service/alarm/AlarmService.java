package com.app.ggumteo.service.alarm;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.domain.alarm.AlarmDTO;

import java.util.List;

public interface AlarmService {

    public void createApplyAuditionAlarm(Long memberProfileId, Long auditionApplicationId, String message, AlarmSubType subType);

    public void createReplyAlarm(Long memberProfileId, Long replyId, String message, AlarmSubType subType);

    public void createWorkAlarm(Long memberProfileId, Long buyWorkId, String message, AlarmSubType subType);

    public void createFundingAlarm(Long memberProfileId, Long buyFundingId, String message, AlarmSubType subType);

    public List<AlarmDTO> getAlarmsByMemberId(Long memberProfileId);

    public List<AlarmDTO> getAlarmsByMemberId7(Long memberProfileId);

    public List<AlarmDTO> getUnreadAlarmsByMemberId(Long memberProfileId);

    public boolean markAlarmAsRead(Long id, Long memberProfileId, String alarmType, Long dataId);
}
