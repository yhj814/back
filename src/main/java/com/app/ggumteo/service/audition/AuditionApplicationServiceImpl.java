package com.app.ggumteo.service.audition;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
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
    public void write(AuditionApplicationDTO auditionApplicationDTO, AlarmSubType subType) {
        log.info("Starting write method for AuditionApplicationDTO: {}", auditionApplicationDTO);

        // 신청 정보 저장
        auditionApplicationDAO.save(auditionApplicationDTO);
        log.info("Saved AuditionApplicationDTO with ID: {}", auditionApplicationDTO.getId());

        // 오디션 정보 조회
        Long auditionId = auditionApplicationDTO.getAuditionId();
        AuditionDTO audition = auditionApplicationDAO.findByAuditionId(auditionId);
        log.info("Fetched AuditionDTO: {}", audition);

        // 알람 생성 조건 확인
        if (audition != null) {
            Long hostMemberProfileId = audition.getMemberProfileId();
            String message = "새로운 오디션 신청이 들어왔습니다.";
            log.info("Creating alarm for hostMemberProfileId: {}", hostMemberProfileId);

            // 알람 생성
            alarmService.createApplyAuditionAlarm(hostMemberProfileId, auditionApplicationDTO.getId(), message, subType);
            log.info("Alarm created for AuditionApplicationId: {}", auditionApplicationDTO.getId());
        } else {
            log.warn("AuditionDTO is null for auditionId: {}", auditionId);
        }

        // 신청 ID 가져오기
        Long auditionApplicationId = auditionApplicationDTO.getId();

        // 업로드된 파일 처리
        List<String> fileNames = auditionApplicationDTO.getFileNames();
        if (fileNames != null && !fileNames.isEmpty()) {
            for (String fileName : fileNames) {
                // 파일 VO 생성 및 설정
                FileVO fileVO = new FileVO();
                fileVO.setFileName(fileName);
                fileVO.setFilePath(getPath());
                File file = new File("C:/upload/" + getPath() + "/" + fileName);
                fileVO.setFileSize(String.valueOf(file.length()));

                try {
                    fileVO.setFileType(Files.probeContentType(file.toPath()));
                } catch (IOException e) {
                    log.error("파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                    fileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
                }

                // 파일 정보 데이터베이스에 저장
                fileDAO.save(fileVO);
                log.info("Saved FileVO with ID: {}", fileVO.getId());

                // 파일과 신청의 연관 관계 설정
                AuditionApplicationFileVO auditionApplicationFileVO = new AuditionApplicationFileVO(fileVO.getId(), auditionApplicationId);
                auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);
                log.info("Linked FileVO ID: {} with AuditionApplicationID: {}", fileVO.getId(), auditionApplicationId);
            }
        }

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
