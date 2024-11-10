package com.app.ggumteo.service.report;


import com.app.ggumteo.domain.report.FundingReportDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface FundingReportService {

//    영상 펀딩 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    List<FundingReportDTO> getFundingVideoReports(String search, String order, AdminPagination pagination);

//    영상 펀딩 신고 목록 전체조회
    int getFundingVideoReportsCount(String search, String order);

//    신고 상태 업데이트
    void updateVideoFundingReportStatus(Long fundingId, String reportStatus);

    //    글 펀딩 신고 목록 조회 (검색, 정렬 ,페이지네이션)
    List<FundingReportDTO> getFundingTextReports(String search, String order, AdminPagination pagination);

    //    글 펀딩 신고 목록 전체조회
    int getFundingTextReportsCount(String search, String order);

    //    글 상태 업데이트
    void updateTextFundingReportStatus(Long fundingId, String reportStatus);
}
