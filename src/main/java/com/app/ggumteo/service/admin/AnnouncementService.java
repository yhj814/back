package com.app.ggumteo.service.admin;

import java.util.List;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.domain.admin.AnnouncementVO;

public interface AnnouncementService {
//    작성
    void write(AnnouncementVO announcementVO);

//    공지사항 전체 조회 (페이지네이션 포함)
    List<AnnouncementVO> getAllAnnouncements(AdminPagination pagination, String order);

//    총 공지사항 수 조회
    int getTotalAnnouncements();

//    공지사항 수정
    void updateAnnouncement(AnnouncementVO announcementVO);

//     공지사항 삭제
    void deleteAnnouncements(List<Integer> ids);
}
