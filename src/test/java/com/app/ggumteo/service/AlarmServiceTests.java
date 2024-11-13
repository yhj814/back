package com.app.ggumteo.service;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.repository.alarm.AlarmDAO;
import com.app.ggumteo.service.alarm.AlarmService;
import com.app.ggumteo.service.audition.AuditionApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AlarmServiceTests는 AlarmServiceImpl 클래스의 단위 테스트를 수행합니다.
 * AlarmDAO는 실제 데이터베이스와 상호작용합니다.
 */
@SpringBootTest
@ActiveProfiles("test") // 'test' 프로파일 사용
@Slf4j
public class AlarmServiceTests {

    @Autowired
    private AlarmService alarmService;

    @Test
    public void testAddApplyAuditionAlarm() {alarmService.createApplyAuditionAlarm(1L,8L,"새로운 모집 신청이 왔습니다.", AlarmSubType.TEXT);}

    @Test
    public void testNewReplyAlarm() {
        alarmService.createReplyAlarm(1L, 1L, "새로운 댓글이 달렸습니다.", AlarmSubType.TEXT);
    }

    @Test
    public void testNewFundingAlarm() {
        alarmService.createFundingAlarm(1L, 1L, "등록한 펀딩이 후원되었습니다.", AlarmSubType.TEXT);
    }

    @Test
    public void testNewWorkAlarm() {
        alarmService.createWorkAlarm(1L, 1L, "등록한 작품이 판매 되었습니다.", AlarmSubType.TEXT);
    }
}
