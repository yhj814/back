package com.app.ggumteo.controller.alarm;

import com.app.ggumteo.domain.alarm.AlarmDTO;
import com.app.ggumteo.service.alarm.AlarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alarm/")
public class AlarmController {
    private AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {this.alarmService = alarmService;}

    @GetMapping("/main/{memberProfileId}")
    public ResponseEntity<List<AlarmDTO>> getAlarmsByMemberProfileId(@PathVariable("memberProfileId") Long memberProfileId) {
        List<AlarmDTO> alarms = alarmService.getAlarmsByMemberId(memberProfileId);
        return ResponseEntity.ok(alarms);
    }

    
}
