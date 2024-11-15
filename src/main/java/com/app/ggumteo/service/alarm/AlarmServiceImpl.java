package com.app.ggumteo.service.alarm;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.repository.alarm.AlarmDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * AlarmServiceImpl은 AlarmService 인터페이스를 구현하며,
 * AlarmDAO를 사용하여 다양한 알림 관련 기능을 제공합니다.
 */
@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AlarmServiceImpl implements AlarmService {
    private final AlarmDAO alarmDAO;

    /*
     * 오디션 신청 알림을 생성합니다.
     */
    @Override
    public void createApplyAuditionAlarm(Long memberProfileId, Long auditionApplicationId, String message, AlarmSubType subType) {
        alarmDAO.saveApplyAuditionAlarm(memberProfileId, auditionApplicationId, message, subType.name());
    }

    /*
     * 댓글 알림을 생성합니다.
     */
    @Override
    public void createReplyAlarm(Long memberProfileId, Long replyId, String message, AlarmSubType subType) {
        alarmDAO.saveReplyAlarm(memberProfileId, replyId, message, subType.name());
    }

    /*
     * 작업 구매 알림을 생성합니다.
     */
    @Override
    public void createWorkAlarm(Long memberProfileId, Long buyWorkId, String message, AlarmSubType subType) {
        alarmDAO.saveWorkAlarm(memberProfileId, buyWorkId, message, subType.name());
    }

    /*
     * 펀딩 상품 구매 알림을 생성합니다.
     */
    @Override
    public void createFundingAlarm(Long memberProfileId, Long buyFundingId, String message, AlarmSubType subType) {
        alarmDAO.saveFundingProductAlarm(memberProfileId, buyFundingId, message, subType.name());
    }

    /*
     * 특정 회원의 모든 알림을 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlarmDTO> getAlarmsByMemberId(Long memberProfileId) {
        List<AlarmDTO> alarms = alarmDAO.findAlarmsByMemberId(memberProfileId);
        return alarms;
    }

    /*
     * 특정 회원의 최신 7개 알림을 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlarmDTO> getAlarmsByMemberId7(Long memberProfileId) {
        List<AlarmDTO> alarms = alarmDAO.findAlarmsByMemberId7(memberProfileId);
        return alarms;
    }

    /*
     * 특정 회원의 읽지 않은 알림을 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlarmDTO> getUnreadAlarmsByMemberId(Long memberProfileId) {
        List<AlarmDTO> unreadAlarms = alarmDAO.findUnreadAlarmsByMemberId(memberProfileId);
        return unreadAlarms;
    }

    /*
     * 특정 알림을 읽은 상태로 업데이트합니다.
     */
    @Override
    public boolean markAlarmAsRead(Long id, Long memberProfileId, String alarmType, Long dataId) {
        int updatedRows = alarmDAO.updateAlarmIsRead(id, memberProfileId, alarmType, dataId);
        if (updatedRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int countUnreadAlarmsBySubtype(Long memberProfileId, String subType) {
        return alarmDAO.countUnreadAlarmsBySubtype(memberProfileId, subType);
    }
}
