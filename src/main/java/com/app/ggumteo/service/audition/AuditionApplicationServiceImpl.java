package com.app.ggumteo.service.audition;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.notification.ApplyAuditionNotificationVO;
import com.app.ggumteo.mapper.audition.AuditionApplicationMapper;
import com.app.ggumteo.mapper.notification.ApplyAuditionNotificationMapper;
import com.app.ggumteo.repository.audition.AuditionApplicationDAO;
import com.app.ggumteo.repository.file.AuditionApplicationFileDAO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.notification.ApplyAuditionNotificationDAO;
import com.app.ggumteo.service.alarm.AlarmService;
import com.app.ggumteo.service.file.AuditionApplicationFileService;
import com.app.ggumteo.service.file.AuditionApplicationFileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditionApplicationServiceImpl implements AuditionApplicationService {

    private final AuditionApplicationDAO auditionApplicationDAO;
    private final ApplyAuditionNotificationDAO applyAuditionNotificationDAO;
    private final FileDAO fileDAO;
    private final AuditionApplicationFileDAO auditionApplicationFileDAO;
    private final AlarmService alarmService;

    @Override
    @Transactional
    public void write(AuditionApplicationDTO auditionApplicationDTO) {
        log.info("Starting write method for AuditionApplicationDTO: {}", auditionApplicationDTO);

        // auditionApplication 테이블에 신청 정보 삽입
        auditionApplicationDAO.save(auditionApplicationDTO);
        log.info("Saved AuditionApplicationDTO with ID: {}", auditionApplicationDTO.getId());

        // auditionId 가져오기
        Long auditionId = auditionApplicationDTO.getAuditionId();
        log.info("Retrieved auditionId from AuditionApplicationDTO: {}", auditionId);

        // AuditionDTO 조회
        AuditionDTO audition = auditionApplicationDAO.findByAuditionId(auditionId);
        log.info("Fetched AuditionDTO: {}", audition);

        // 알림 생성 조건 확인
        if (audition != null) {
            Long hostMemberProfileId = audition.getMemberProfileId();
            String message = "새로운 오디션 신청이 들어왔습니다.";
            log.info("Creating alarm for hostMemberProfileId: {}", hostMemberProfileId);

            // 알림 생성
            alarmService.createApplyAuditionAlarm(hostMemberProfileId, auditionApplicationDTO.getId(), message, AlarmSubType.);
            log.info("Alarm created for AuditionApplicationId: {}", auditionApplicationDTO.getId());
        } else {
            log.warn("AuditionDTO is null for auditionId: {}", auditionId);
        }

        // 파일 처리 로직 (생략)
        // ...

        log.info("Completed write method for AuditionApplicationDTO ID: {}", auditionApplicationDTO.getId());
    }

    @Override
    public int countApplicantsByAuditionId(Long auditionId) {
        return auditionApplicationDAO.countApplicantsByAuditionId(auditionId);
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}



