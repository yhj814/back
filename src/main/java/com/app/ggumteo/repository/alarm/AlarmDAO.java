package com.app.ggumteo.repository.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.mapper.alarm.AlarmMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 오디션 신청 알림을 저장합니다.
     *
     * @param memberProfileId        회원 프로필 ID
     * @param auditionApplicationId  오디션 신청 ID
     * @param message                알림 메시지
     */
    public void saveApplyAuditionAlarm(Long memberProfileId, Long auditionApplicationId, String message, String subType) {
        alarmMapper.insertApplyAuditionAlarm(memberProfileId, auditionApplicationId, message, subType);
        log.info("Saved Apply Audition Alarm for MemberProfileId: {}, AuditionApplicationId: {}", memberProfileId, auditionApplicationId);
    }

    /**
     * 댓글 알림을 저장합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @param replyId         댓글 ID
     * @param message         알림 메시지
     */
    public void saveReplyAlarm(Long memberProfileId, Long replyId, String message, String subType) {
        alarmMapper.insertReplyAlarm(memberProfileId, replyId, message, subType);
        log.info("Saved Reply Alarm for MemberProfileId: {}, ReplyId: {}", memberProfileId, replyId);
    }

    /**
     * 작업 구매 알림을 저장합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @param buyWorkId       구매 작업 ID
     * @param message         알림 메시지
     */
    public void saveWorkAlarm(Long memberProfileId, Long buyWorkId, String message, String subType) {
        alarmMapper.insertWorkAlarm(memberProfileId, buyWorkId, message, subType);
        log.info("Saved Work Alarm for MemberProfileId: {}, BuyWorkId: {}", memberProfileId, buyWorkId);
    }

    /**
     * 펀딩 상품 구매 알림을 저장합니다.
     *
     * @param memberProfileId        회원 프로필 ID
     * @param buyFundingProductId    구매 펀딩 상품 ID
     * @param message                알림 메시지
     */
    public void saveFundingProductAlarm(Long memberProfileId, Long buyFundingProductId, String message, String subType) {
        alarmMapper.insertFundingProductAlarm(memberProfileId, buyFundingProductId, message, subType);
        log.info("Saved Funding Product Alarm for MemberProfileId: {}, BuyFundingProductId: {}", memberProfileId, buyFundingProductId);
    }

    /**
     * 특정 회원의 모든 알림을 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @return 알림 목록
     */
    public List<AlarmDTO> findAlarmsByMemberId(Long memberProfileId) {
        List<AlarmDTO> alarms = alarmMapper.selectAlarmsByMemberId(memberProfileId);
        log.info("Retrieved {} alarms for MemberProfileId: {}", alarms.size(), memberProfileId);
        return alarms;
    }

    /**
     * 특정 회원의 최신 7개 알림을 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @return 알림 목록
     */
    public List<AlarmDTO> findAlarmsByMemberId7(Long memberProfileId) {
        List<AlarmDTO> alarms = alarmMapper.selectAlarmsByMemberId7(memberProfileId);
        log.info("Retrieved latest 7 alarms for MemberProfileId: {}", memberProfileId);
        return alarms;
    }

    /**
     * 특정 회원의 읽지 않은 알림을 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @return 읽지 않은 알림 목록
     */
    public List<AlarmDTO> findUnreadAlarmsByMemberId(Long memberProfileId) {
        List<AlarmDTO> unreadAlarms = alarmMapper.selectUnreadAlarmsByMemberId(memberProfileId);
        log.info("Retrieved {} unread alarms for MemberProfileId: {}", unreadAlarms.size(), memberProfileId);
        return unreadAlarms;
    }

    /**
     * 특정 알림을 읽은 상태로 업데이트합니다.
     *
     * @param id              알림 ID
     * @param memberProfileId 회원 프로필 ID
     * @param alarmType       알림 유형
     * @param dataId          관련 게시물 ID
     * @return 업데이트된 행의 수
     */
    public int updateAlarmIsRead(Long id, Long memberProfileId, String alarmType, Long dataId) {
        int updatedRows = alarmMapper.updateAlarmIsRead(id, memberProfileId, alarmType, dataId);
        if (updatedRows > 0) {
            log.info("Successfully updated alarm as read. ID: {}, MemberProfileId: {}, AlarmType: {}, PostId: {}", id, memberProfileId, alarmType, dataId);
        } else {
            log.warn("Failed to update alarm as read. ID: {}, MemberProfileId: {}, AlarmType: {}, PostId: {}", id, memberProfileId, alarmType, dataId);
        }
        return updatedRows;
    }

    /**
     * 특정 회원의 특정 subType에 대한 읽지 않은 알람 수를 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @param subType         알람 서브 타입
     * @return 읽지 않은 알람 수
     */
    public int countUnreadAlarmsBySubtype(Long memberProfileId, String subType) {
        int count = alarmMapper.countUnreadAlarmsBySubtype(memberProfileId, subType);
        log.info("Unread alarms count for MemberProfileId: {}, SubType: {}: {}", memberProfileId, subType, count);
        return count;
    }
}
