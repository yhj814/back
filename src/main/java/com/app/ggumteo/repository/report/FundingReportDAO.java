package com.app.ggumteo.repository.report;

import com.app.ggumteo.domain.report.FundingReportDTO;
import com.app.ggumteo.mapper.report.FundingReportMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FundingReportDAO {
    private final FundingReportMapper fundingReportMapper;

//    영상 펀딩 신고 목록 조회
    public List<FundingReportDTO> getFundingVideoReports(String search, String order, AdminPagination pagination) {
        return fundingReportMapper.selectFundingVideoReports(search, order, pagination);
    }

//    영상 펀딩 신고 목록 전체 조회
    public int getFundingVideoReportsCount(String search, String order) {
        return fundingReportMapper.fundingVideoReportCounts(search, order);
    }

//    신고 상태 업데이트
    public void updateVideoFundingReportStatus(Long fundingId, String reportStatus) {
        fundingReportMapper.fundingVideoStatusChange(fundingId, reportStatus);
    }

    //    글 펀딩 신고 목록 조회
    public List<FundingReportDTO> getFundingTextReports(String search, String order, AdminPagination pagination) {
        return fundingReportMapper.selectFundingTextReports(search, order, pagination);
    }

    //    글 펀딩 신고 목록 전체 조회
    public int getFundingTextReportsCount(String search, String order) {
        return fundingReportMapper.fundingTextReportCounts(search, order);
    }

    //    글 상태 업데이트
    public void updateTextFundingReportStatus(Long fundingId, String reportStatus) {
        fundingReportMapper.fundingTextStatusChange(fundingId, reportStatus);
    }
}
