package com.app.ggumteo.repository.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.mapper.alarm.AlarmMapper;
import com.app.ggumteo.pagination.MyAlarmPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AlarmDAO는 AlarmMapper를 사용하여 데이터베이스와의 상호작용을 담당합니다.
 * 다양한 알림 유형에 대한 삽입, 조회, 업데이트 기능을 제공합니다.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AlarmDAO {
    private final AlarmMapper alarmMapper;

    /*
     * 오디션 신청 알림을 저장합니다.
     */
    public void saveApplyAuditionAlarm(Long memberProfileId, Long auditionApplicationId, String message, String subType) {
        alarmMapper.insertApplyAuditionAlarm(memberProfileId, auditionApplicationId, message, subType);
    }

    /*
     * 댓글 알림을 저장합니다.
     */
    public void saveReplyAlarm(Long memberProfileId, Long replyId, String message, String subType) {
        alarmMapper.insertReplyAlarm(memberProfileId, replyId, message, subType);
    }

    /*
     * 작업 구매 알림을 저장합니다.
     */
    public void saveWorkAlarm(Long memberProfileId, Long buyWorkId, String message, String subType) {
        alarmMapper.insertWorkAlarm(memberProfileId, buyWorkId, message, subType);
    }

    /*
     * 펀딩 상품 구매 알림을 저장합니다.
     */
    public void saveFundingProductAlarm(Long memberProfileId, Long buyFundingProductId, String message, String subType) {
        alarmMapper.insertFundingProductAlarm(memberProfileId, buyFundingProductId, message, subType);
    }

    /*
     * 특정 회원의 모든 알림을 조회합니다.
     */
    public List<AlarmDTO> findAlarmsByMemberId(Long memberProfileId) {
        List<AlarmDTO> alarms = alarmMapper.selectAlarmsByMemberId(memberProfileId);
        return alarms;
    }

    /*
     * 특정 회원의 최신 7개 알림을 조회합니다.
     */
    public List<AlarmDTO> findAlarmsByMemberId7(Long memberProfileId) {
        List<AlarmDTO> alarms = alarmMapper.selectAlarmsByMemberId7(memberProfileId);
        return alarms;
    }

    /*
     * 특정 회원의 읽지 않은 알림을 조회합니다.
     */
    public List<AlarmDTO> findUnreadAlarmsByMemberId(Long memberProfileId) {
        List<AlarmDTO> unreadAlarms = alarmMapper.selectUnreadAlarmsByMemberId(memberProfileId);
        return unreadAlarms;
    }

    /*
     * 특정 알림을 읽은 상태로 업데이트합니다.
     */
    public int updateAlarmIsRead(Long id, Long memberProfileId, String alarmType, Long dataId) {
        int updatedRows = alarmMapper.updateAlarmIsRead(id, memberProfileId, alarmType, dataId);
        return updatedRows;
    }

    /**
     * 특정 회원의 특정 subType에 대한 읽지 않은 알람 수를 조회합니다.
     */
    public int countUnreadAlarmsBySubtype(Long memberProfileId, String subType) {
        int count = alarmMapper.countUnreadAlarmsBySubtype(memberProfileId, subType);
        return count;
    }

    public List<AlarmDTO> findAlarmsByMemberProfileId(MyAlarmPagination myAlarmPagination,
                                                      Long memberProfileId, String subType) {
        return alarmMapper.selectAlarmsByMemberProfileId(myAlarmPagination, memberProfileId, subType);
    };

    public int getAlarmTotal(Long memberProfileId, String subType) {
        return alarmMapper.selectAlarmTotal(memberProfileId, subType);
    };
}
