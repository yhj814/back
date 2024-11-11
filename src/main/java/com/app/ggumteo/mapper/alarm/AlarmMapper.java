package com.app.ggumteo.mapper.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmMapper {

    void insertApplyAuditionAlarm(@Param("memberProfileId") Long memberProfileId, @Param("auditionApplicationId") Long auditionApplicationId, @Param("message") String message);

    public List<AlarmDTO> selectAlarmsByMemberId(@Param("memberProfileId") Long memberProfileId);
}