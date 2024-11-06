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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuditionApplicationServiceImpl implements AuditionApplicationService {

    private final AuditionApplicationDAO auditionApplicationDAO;
    private final ApplyAuditionNotificationDAO applyAuditionNotificationDAO;
    private final AuditionApplicationFileDAO auditionApplicationFileDAO;
    private final AuditionApplicationFileService auditionApplicationFileService;
    private final AuditionApplicationFileVO auditionApplicationFileVO;

    @Transactional
    @Override
    public void write(AuditionApplicationDTO auditionApplicationDTO, MultipartFile applicationfile) {
        // 1. auditionApplication 테이블에 신청 정보 삽입
        auditionApplicationDAO.save(auditionApplicationDTO);

        // 2. 방금 저장된 신청 정보의 ID 가져오기
        Long auditionApplicationId = auditionApplicationDTO.getId();

        // 3. 알림 테이블에 알림 추가
        ApplyAuditionNotificationVO notificationVO = new ApplyAuditionNotificationVO(auditionApplicationId);
        applyAuditionNotificationDAO.save(auditionApplicationId);

        // 필요 시 파일 첨부 로직 추가
        if (applicationfile != null) {
            auditionApplicationFileService.saveAuditionApplicationFile(applicationfile, auditionApplicationDTO.getId());
        }
    }

}
