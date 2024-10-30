package com.app.ggumteo.mapper;
import com.app.ggumteo.domain.admin.AnnouncementVO;
import com.app.ggumteo.mapper.admin.AnnouncementMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AnnouncementMapperTests {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Test
    public void testInsertMultipleAnnouncements() {

            AnnouncementVO announcementVO = new AnnouncementVO();
            announcementVO.setAnnouncementTitle("공지사항 제목 5");
            announcementVO.setAnnouncementContent("공지사항 내용 5");

            // 메퍼를 호출하여 데이터 삽입
            announcementMapper.insert(announcementVO);

            // 삽입 후 확인을 위해 로그 출력
            log.info("공지사항이 삽입되었습니다: {}", announcementVO);

    }
}


