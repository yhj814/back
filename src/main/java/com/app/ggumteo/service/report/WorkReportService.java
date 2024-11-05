package com.app.ggumteo.service.report;


import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface WorkReportService {

    //    영상 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    List<WorkReportDTO> getVideoReports(String search, String order, AdminPagination pagination);

    //    영상 신고 목록 전체조회
    int getVideoReportsCount(String search, String order);
}
