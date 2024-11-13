package com.app.ggumteo.interceptor;

import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.service.alarm.AlarmService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Slf4j
@Component
public class AlarmIntercepter implements HandlerInterceptor {
    private final AlarmService alarmService;

    public AlarmIntercepter(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션 속성 로그 출력
        HttpSession session = request.getSession(false);
        if (session != null) {
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);
                log.info("Session attribute: {} = {}", attributeName, attributeValue);
            }
        } else {
            log.info("세션이 존재하지 않습니다.");
        }

        // "memberProfile" 속성을 가져옵니다.
        MemberProfileDTO memberProfile = (MemberProfileDTO) request.getSession().getAttribute("memberProfile");

        if (memberProfile != null) {
            Long memberProfileId = memberProfile.getId();

            // 읽지 않은 알람 목록 설정
            var unreadAlarms = alarmService.getUnreadAlarmsByMemberId(memberProfileId);
            request.setAttribute("unreadAlarms", unreadAlarms);

            // 모든 알람 목록 설정
            var allAlarms = alarmService.getAlarmsByMemberId(memberProfileId);
            request.setAttribute("allAlarms", allAlarms);

            // subType별 읽지 않은 알람 수 설정
            int unreadVideoAlarmsCount = alarmService.countUnreadAlarmsBySubtype(memberProfileId, "VIDEO");
            int unreadTextAlarmsCount = alarmService.countUnreadAlarmsBySubtype(memberProfileId, "TEXT");

            request.setAttribute("unreadVideoAlarmsCount", unreadVideoAlarmsCount);
            request.setAttribute("unreadTextAlarmsCount", unreadTextAlarmsCount);

            log.info("로그인 된 사용자. memberProfileId: {}", memberProfileId);
        } else {
            log.info("로그인 되지 않은 사용자입니다.");
        }
        return true;
    }
}
