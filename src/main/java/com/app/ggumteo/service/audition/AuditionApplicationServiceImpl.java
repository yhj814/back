package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.notification.ApplyAuditionNotificationVO;
import com.app.ggumteo.mapper.audition.AuditionApplicationMapper;
import com.app.ggumteo.mapper.notification.ApplyAuditionNotificationMapper;
import com.app.ggumteo.repository.audition.AuditionApplicationDAO;
import com.app.ggumteo.repository.file.AuditionApplicationFileDAO;
import com.app.ggumteo.repository.notification.ApplyAuditionNotificationDAO;
import com.app.ggumteo.service.file.AuditionApplicationFileService;
import com.app.ggumteo.service.file.AuditionApplicationFileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditionApplicationServiceImpl implements AuditionApplicationService {

    private final AuditionApplicationDAO auditionApplicationDAO;
    private final ApplyAuditionNotificationDAO applyAuditionNotificationDAO;
    private final AuditionApplicationFileDAO auditionApplicationFileDAO;
    private final AuditionApplicationFileService auditionApplicationFileService;

    @Override
    @Transactional
    public void write(AuditionApplicationDTO auditionApplicationDTO) {
        // auditionApplication 테이블에 신청 정보 삽입
        auditionApplicationDAO.save(auditionApplicationDTO);

        // 방금 저장된 신청 정보의 ID 가져오기 (삽입 완료 후에 ID를 사용)
        Long auditionApplicationId = auditionApplicationDTO.getId();
        log.info("Generated auditionApplicationId: {}", auditionApplicationId);

        // 알림 테이블에 알림 추가
        ApplyAuditionNotificationVO notificationVO = new ApplyAuditionNotificationVO();
        notificationVO.setAuditionApplicationId(auditionApplicationId); // ID 설정
        applyAuditionNotificationDAO.save(notificationVO);

        log.info("Notification saved with auditionApplicationId: {}", auditionApplicationId);
    }

    @Override
    public int countApplicantsByAuditionId(Long auditionId) {
        return auditionApplicationDAO.countApplicantsByAuditionId(auditionId);
    }
}

