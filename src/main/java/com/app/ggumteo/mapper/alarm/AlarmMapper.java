package com.app.ggumteo.mapper.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmMapper {

    void insertApplyAuditionAlarm(@Param("memberProfileId") Long memberProfileId, @Param("auditionApplicationId") Long auditionApplicationId, @Param("message") String message, @Param("subType") String subType);

    void insertReplyAlarm(@Param("memberProfileId") Long memberProfileId, @Param("replyId") Long replyId, @Param("message") String message, @Param("subType") String subType);

    void insertWorkAlarm(@Param("memberProfileId") Long memberProfileId, @Param("buyWorkId") Long buyWorkId, @Param("message") String message, @Param("subType") String subType);

    void insertFundingProductAlarm(@Param("memberProfileId") Long memberProfileId, @Param("buyFundingProductId") Long buyFundingProductId, @Param("message") String message, @Param("subType") String subType);

    public List<AlarmDTO> selectAlarmsByMemberId(@Param("memberProfileId") Long memberProfileId);

    public List<AlarmDTO> selectAlarmsByMemberId7(@Param("memberProfileId") Long memberProfileId);

    public List<AlarmDTO> selectUnreadAlarmsByMemberId(@Param("memberProfileId") Long memberId);

    public int updateAlarmIsRead(
            @Param("id") Long id,
            @Param("memberProfileId") Long memberId,
            @Param("alarmType") String alarmType,
            @Param("dataId") Long dataId
    );

    int countUnreadAlarmsBySubtype(@Param("memberProfileId") Long memberProfileId, @Param("subType") String subType);
}