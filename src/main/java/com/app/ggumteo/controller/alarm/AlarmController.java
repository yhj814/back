package com.app.ggumteo.controller.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.service.alarm.AlarmService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alarm")
@Slf4j
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }


     //알림 버튼 클릭 시 마이페이지-알람으로
    @GetMapping("/video/my-page")
    public RedirectView clickAlarmButton() {
        return new RedirectView("/member/video/my-page?category=alarm");
    }
    /**
     * 현재 로그인한 회원의 모든 알림을 조회합니다.
     *
     * @param session HTTP 세션
     * @return 알림 목록
     */
    @GetMapping("/mypage-member")
    @ResponseBody
    public ResponseEntity<List<AlarmDTO>> getAlarmsByCurrentMember(HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        if (member != null) {
            List<AlarmDTO> alarms = alarmService.getAlarmsByMemberId(member.getId());
            return ResponseEntity.ok(alarms);
        } else {
            log.warn("로그인되지 않은 사용자가 알림을 조회하려고 시도했습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 현재 로그인한 회원의 읽지 않은 알림을 조회합니다.
     *
     * @param session HTTP 세션
     * @return 읽지 않은 알림 목록
     */
    @GetMapping("/member/unread")
    @ResponseBody
    public ResponseEntity<List<AlarmDTO>> getUnreadAlarmsByCurrentMember(HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        if (member != null) {
            List<AlarmDTO> latestAlarms = alarmService.getUnreadAlarmsByMemberId(member.getId());
            return ResponseEntity.ok(latestAlarms);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 특정 알림을 읽은 상태로 업데이트합니다.
     *
     * @param id         알림 ID
     * @param alarmData 알림 데이터 (alarmType, dataId)
     * @param session    HTTP 세션
     * @return 상태 코드
     */
    @PutMapping("/{id}/read")
    @ResponseBody
    public ResponseEntity<Void> markAlarmAsRead(
            @PathVariable Long id,
            @RequestBody Map<String, Object> alarmData,
            HttpSession session
    ) {
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        if (member != null) {
            Long memberId = member.getId();
            String alarmType = (String) alarmData.get("alarmType");

            Long dataId = null;
            if (alarmData.get("dataId") instanceof Number) {
                dataId = ((Number) alarmData.get("dataId")).longValue();
            } else if (alarmData.get("postId") instanceof Number) { // postId로도 받을 수 있음
                dataId = ((Number) alarmData.get("postId")).longValue();
            }

            if (alarmType == null || alarmType.isEmpty() || dataId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            boolean success = alarmService.markAlarmAsRead(id, memberId, alarmType, dataId);
            if (success) {
                log.info("알림 ID {}가 회원 ID {}에 의해 읽음 처리되었습니다.", id, memberId);
                return ResponseEntity.ok().build();
            } else {
                log.warn("알림 ID {}를 회원 ID {}에 의해 읽음 처리하지 못했습니다.", id, memberId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            log.warn("로그인되지 않은 사용자가 알림을 읽음 처리하려고 시도했습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 알림을 읽음 처리하고 마이페이지로 리디렉션합니다.
     *
     * @param id        알림 ID
     * @param postId    관련 게시물 ID
     * @param alarmType 알림 유형
     * @param session   HTTP 세션
     * @return 마이페이지 RedirectView
     */
    @GetMapping("/read/{id}")
    public RedirectView readAlarm(
            @PathVariable Long id,
            @RequestParam Long postId,
            @RequestParam String alarmType,
            HttpSession session
    ) {
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        if (member != null) {
            Long memberId = member.getId();
            boolean success = alarmService.markAlarmAsRead(id, memberId, alarmType, postId);
            if (success) {
                log.info("알림 ID {}가 회원 ID {}에 의해 읽음 처리되었습니다.", id, memberId);
            } else {
                log.warn("알림 ID {}를 회원 ID {}에 의해 읽음 처리하지 못했습니다.", id, memberId);
            }
        } else {
            log.warn("로그인되지 않은 사용자가 알림을 읽음 처리하려고 시도했습니다.");
        }

        // 알림을 읽은 후 마이페이지로 이동
        return new RedirectView("/mypage/mypage");
    }
}
