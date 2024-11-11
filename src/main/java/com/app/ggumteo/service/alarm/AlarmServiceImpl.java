package com.app.ggumteo.service.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.repository.alarm.AlarmDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AlarmServiceImpl implements AlarmService {
    private final AlarmDAO alarmDAO;

    @Override
    public void createApplyAuditionAlarm(Long memberProfileId, Long auditionApplicationId, String message) {
        alarmDAO.saveApplyAudition(memberProfileId, auditionApplicationId, message);
    }

    @Override
    public List<AlarmDTO> getAlarmsByMemberId(Long memberProfileId) { return alarmDAO.findAlarmsByMemberId(memberProfileId); }
}
