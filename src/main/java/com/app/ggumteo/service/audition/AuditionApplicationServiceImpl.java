package com.app.ggumteo.service.audition;

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
        // auditionApplication 테이블에 신청 정보 삽입
        auditionApplicationDAO.save(auditionApplicationDTO);

        // 방금 저장된 신청 정보의 ID 가져오기 (삽입 완료 후에 ID를 사용)
        Long auditionApplicationId = auditionApplicationDTO.getId();
        log.info("Generated auditionApplicationId: {}", auditionApplicationId);

        // 알림 테이블에 알림 추가
        // 알림 생성: 오디션 주최자에게 알림 전송
        Long auditionId = auditionApplicationDTO.getAuditionId(); // 오디션 ID
        // 오디션 정보를 조회하여 주최자의 프로필 ID를 가져옵니다.
        AuditionDTO audition = auditionApplicationDAO.findByAuditionId(auditionId);
        if (audition != null) {
            Long hostMemberProfileId = audition.getMemberProfileId(); // 주최자의 프로필 ID
            String message = "새로운 오디션 신청이 들어왔습니다.";
            alarmService.createApplyAuditionAlarm(hostMemberProfileId, auditionApplicationId, message);
            log.info("Notification created for HostMemberProfileId: {}, AuditionApplicationId: {}", hostMemberProfileId, auditionApplicationId);
        } else {
            log.warn("해당 오디션을 찾을 수 없습니다. AuditionId: {}", auditionId);
        }

        log.info("Notification saved with auditionApplicationId: {}", auditionApplicationId);

        // 업로드된 파일 처리
        List<String> fileNames = auditionApplicationDTO.getFileNames();
        if (fileNames != null && !fileNames.isEmpty()) {
            for (String fileName : fileNames) {
                // 기존 메서드 사용
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

                // 파일과 게시글의 연관 관계 설정
                AuditionApplicationFileVO auditionApplicationFileVO = new AuditionApplicationFileVO(fileVO.getId(), auditionApplicationId);
                auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);

            }
        }
    }

    @Override
    public int countApplicantsByAuditionId(Long auditionId) {
        return auditionApplicationDAO.countApplicantsByAuditionId(auditionId);
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}

