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

//    신고 상태 업데이트
    public void updateReportStatus(Long workId, String reportStatus) {
        workReportMapper.videoStatusChange(workId, reportStatus);
    }

//    글 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    public List<WorkReportDTO> getTextReports(String search, String order, AdminPagination pagination) {
        return workReportMapper.selectTextReports(search, order, pagination);
    }

//    글 신고 목록 전체조회
    public int getTextReportsCount(String search, String order) {
        return workReportMapper.countTextReports(search,order);
    }

//    글 상태 업데이트
    public void updateTextReportStatus(Long workId, String reportStatus) {
        workReportMapper.textStatusChange(workId, reportStatus);
    }
}
