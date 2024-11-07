package com.app.ggumteo.service.report;

import com.app.ggumteo.domain.report.AuditionReportDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface AuditionReportService {

//    영상 모집글 신고 목록
    List<AuditionReportDTO> getVideoAuditionReports(String search, String order, AdminPagination pagination);

//    영상 모집글 신고 목록 카운트
    int getVideoAuditionReportCount(String search, String order);

//    영상 모집글 신고 상태 업데이트
    void updateVideoAuditionReportStatus(Long auditionId, String reportStatus);
}
