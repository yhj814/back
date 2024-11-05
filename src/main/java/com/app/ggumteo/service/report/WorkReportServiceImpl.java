package com.app.ggumteo.service.report;

import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.report.WorkReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class WorkReportServiceImpl implements WorkReportService {
    private final WorkReportDAO workReportDAO;

    //    영상 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    @Override
    public List<WorkReportDTO> getVideoReports(String search, String order, AdminPagination pagination) {
        return workReportDAO.getVideoReports(search, order, pagination);
    }

    //    영상 신고 목록 전체조회
    @Override
    public int getVideoReportsCount(String search, String order) {
        return workReportDAO.getVideoReportsCount(search, order);
    }
}
