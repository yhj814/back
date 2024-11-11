package com.app.ggumteo.repository.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.mapper.alarm.AlarmMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmDAO {
    private final AlarmMapper alarmMapper;

    public void saveApplyAudition(Long memberProfileId, Long auditionApplicationId, String message) {
        alarmMapper.insertApplyAuditionAlarm(memberProfileId, auditionApplicationId, message);
    }

    public List<AlarmDTO> findAlarmsByMemberId(Long memberProfileId) { return alarmMapper.selectAlarmsByMemberId(memberProfileId);}
}
