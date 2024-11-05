package com.app.ggumteo.repository.report;

import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.mapper.report.WorkReportMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkReportDAO {
    private final WorkReportMapper workReportMapper;

//    영상 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    public List<WorkReportDTO> getVideoReports(String search, String order, AdminPagination pagination) {
        return workReportMapper.selectVideoReports(search, order, pagination);
    }

//    영상 신고 목록 전체조회
    public int getVideoReportsCount(String search, String order) {
        return workReportMapper.countVideoReports(search,order);
    }
}
