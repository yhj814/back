package com.app.ggumteo.service.report;


import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface WorkReportService {

    //    영상 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    List<WorkReportDTO> getVideoReports(String search, String order, AdminPagination pagination);

    //    영상 신고 목록 전체조회
    int getVideoReportsCount(String search, String order);

    //    신고 상태 업데이트
    void updateReportStatus(Long workId, String reportStatus);

    //    글 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    List<WorkReportDTO> getTextReports(String search, String order, AdminPagination pagination);

    //    글 신고 목록 전체조회
    int getTextReportsCount(String search, String order);

    //    글 상태 업데이트
    void updateTextReportStatus(Long workId, String reportStatus);
}
