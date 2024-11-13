package com.app.ggumteo.interceptor;

import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.service.alarm.AlarmService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AlarmIntercepter implements HandlerInterceptor {
    private final AlarmService alarmService;

    public AlarmIntercepter(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션에서 "memberProfile" 속성을 가져옵니다.
        MemberProfileDTO memberProfile = (MemberProfileDTO) request.getSession().getAttribute("memberProfile");

        if (memberProfile != null) {
            Long memberProfileId = memberProfile.getId();
            var unreadAlarms = alarmService.getUnreadAlarmsByMemberId(memberProfileId);
            request.setAttribute("unreadAlarms", unreadAlarms);

            var allAlarms = alarmService.getAlarmsByMemberId(memberProfileId);
            request.setAttribute("allAlarms", allAlarms);

            log.info("로그인 된 사용자. memberProfileId: {}", memberProfileId);
        } else {
            log.info("로그인 되지 않은 사용자입니다.");
        }
        return true;
    }
}
