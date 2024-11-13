package com.app.ggumteo.service.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.repository.alarm.AlarmDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
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

    /**
     * 오디션 신청 알림을 생성합니다.
     *
     * @param memberProfileId        회원 프로필 ID
     * @param auditionApplicationId  오디션 신청 ID
     * @param message                알림 메시지
     */
    @Override
    public void createApplyAuditionAlarm(Long memberProfileId, Long auditionApplicationId, String message) {
        log.info("Creating Apply Audition Alarm. MemberProfileId: {}, AuditionApplicationId: {}", memberProfileId, auditionApplicationId);
        alarmDAO.saveApplyAuditionAlarm(memberProfileId, auditionApplicationId, message);
        log.info("Successfully created Apply Audition Alarm for MemberProfileId: {}, AuditionApplicationId: {}", memberProfileId, auditionApplicationId);
    }

    /**
     * 댓글 알림을 생성합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @param replyId         댓글 ID
     * @param message         알림 메시지
     */
    @Override
    public void createReplyAlarm(Long memberProfileId, Long replyId, String message) {
        log.info("Creating Reply Alarm. MemberProfileId: {}, ReplyId: {}", memberProfileId, replyId);
        alarmDAO.saveReplyAlarm(memberProfileId, replyId, message);
        log.info("Successfully created Reply Alarm for MemberProfileId: {}, ReplyId: {}", memberProfileId, replyId);
    }

    /**
     * 작업 구매 알림을 생성합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @param buyWorkId       구매 작업 ID
     * @param message         알림 메시지
     */
    @Override
    public void createWorkAlarm(Long memberProfileId, Long buyWorkId, String message) {
        log.info("Creating Work Alarm. MemberProfileId: {}, BuyWorkId: {}", memberProfileId, buyWorkId);
        alarmDAO.saveWorkAlarm(memberProfileId, buyWorkId, message);
        log.info("Successfully created Work Alarm for MemberProfileId: {}, BuyWorkId: {}", memberProfileId, buyWorkId);
    }

    /**
     * 펀딩 상품 구매 알림을 생성합니다.
     *
     * @param memberProfileId    회원 프로필 ID
     * @param buyFundingId       구매 펀딩 상품 ID
     * @param message            알림 메시지
     */
    @Override
    public void createFundingAlarm(Long memberProfileId, Long buyFundingId, String message) {
        log.info("Creating Funding Product Alarm. MemberProfileId: {}, BuyFundingId: {}", memberProfileId, buyFundingId);
        alarmDAO.saveFundingProductAlarm(memberProfileId, buyFundingId, message);
        log.info("Successfully created Funding Product Alarm for MemberProfileId: {}, BuyFundingId: {}", memberProfileId, buyFundingId);
    }

    /**
     * 특정 회원의 모든 알림을 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @return 알림 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlarmDTO> getAlarmsByMemberId(Long memberProfileId) {
        log.info("Retrieving all alarms for MemberProfileId: {}", memberProfileId);
        List<AlarmDTO> alarms = alarmDAO.findAlarmsByMemberId(memberProfileId);
        log.info("Retrieved {} alarms for MemberProfileId: {}", alarms.size(), memberProfileId);
        return alarms;
    }

    /**
     * 특정 회원의 최신 7개 알림을 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @return 알림 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlarmDTO> getAlarmsByMemberId7(Long memberProfileId) {
        log.info("Retrieving latest 7 alarms for MemberProfileId: {}", memberProfileId);
        List<AlarmDTO> alarms = alarmDAO.findAlarmsByMemberId7(memberProfileId);
        log.info("Retrieved {} alarms for MemberProfileId: {}", alarms.size(), memberProfileId);
        return alarms;
    }

    /**
     * 특정 회원의 읽지 않은 알림을 조회합니다.
     *
     * @param memberProfileId 회원 프로필 ID
     * @return 읽지 않은 알림 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlarmDTO> getUnreadAlarmsByMemberId(Long memberProfileId) {
        log.info("Retrieving unread alarms for MemberProfileId: {}", memberProfileId);
        List<AlarmDTO> unreadAlarms = alarmDAO.findUnreadAlarmsByMemberId(memberProfileId);
        log.info("Retrieved {} unread alarms for MemberProfileId: {}", unreadAlarms.size(), memberProfileId);
        return unreadAlarms;
    }

    /**
     * 특정 알림을 읽은 상태로 업데이트합니다.
     *
     * @param id              알림 ID
     * @param memberProfileId 회원 프로필 ID
     * @param alarmType       알림 유형
     * @param dataId          관련 데이터 ID (postId 또는 기타 ID)
     * @return 업데이트 성공 여부
     */
    @Override
    public boolean markAlarmAsRead(Long id, Long memberProfileId, String alarmType, Long dataId) {
        log.info("Marking alarm as read. ID: {}, MemberProfileId: {}, AlarmType: {}, DataId: {}", id, memberProfileId, alarmType, dataId);
        int updatedRows = alarmDAO.updateAlarmIsRead(id, memberProfileId, alarmType, dataId);
        if (updatedRows > 0) {
            log.info("Successfully marked alarm as read. ID: {}", id);
            return true;
        } else {
            log.warn("Failed to mark alarm as read. ID: {}", id);
            return false;
        }
    }
}
