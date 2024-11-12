package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.notification.ApplyAuditionNotificationVO;
import com.app.ggumteo.mapper.audition.AuditionApplicationMapper;
import com.app.ggumteo.mapper.notification.ApplyAuditionNotificationMapper;
import com.app.ggumteo.service.audition.AuditionApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
public class AuditionApplicationMapperTests {

    @Autowired
    private AuditionApplicationMapper auditionApplicationMapper;
    @Autowired
    private AuditionApplicationService auditionApplicationService;
    @Autowired
    private ApplyAuditionNotificationMapper applyAuditionNotificationMapper;
    @Autowired
    private ApplyAuditionNotificationVO applyAuditionNotificationVO;

    @Test
    public void testInsertAuditionApplication() {
        // Given: 이미 존재하는 외래 키 ID 사용
        Long existingMemberProfileId = 20L;
        Long existingAuditionId = 90L;

        // AuditionApplicationDTO 설정
        AuditionApplicationDTO auditionApplicationDTO = new AuditionApplicationDTO();
        auditionApplicationDTO.setMemberProfileId(existingMemberProfileId); // 존재하는 profile ID 사용
        auditionApplicationDTO.setApplyEtc("Test ETC");
        auditionApplicationDTO.setAuditionId(existingAuditionId); // 존재하는 audition ID 사용
        auditionApplicationDTO.setConfirmStatus("NO");

        // When: Service를 통해 AuditionApplication과 Notification 삽입
//        auditionApplicationService.write(auditionApplicationDTO, null);

        // Then: ID가 자동 생성되었는지 검증
        Assertions.assertNotNull(auditionApplicationDTO.getId());
        log.info("삽입된 AuditionApplication ID: {}", auditionApplicationDTO.getId());

    }
}
