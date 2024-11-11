package com.app.ggumteo.service.report;

import com.app.ggumteo.domain.report.FundingReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.report.FundingReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class FundingReportServiceImpl implements FundingReportService {
    private final FundingReportDAO fundingReportDAO;

//    영상 펀딩 신고 목록조회
    @Override
    public List<FundingReportDTO> getFundingVideoReports(String search, String order, AdminPagination pagination){
        return fundingReportDAO.getFundingVideoReports(search, order, pagination);
    }

//    영상 펀딩 신고 전체조회
    @Override
    public int getFundingVideoReportsCount(String search, String order){
        return fundingReportDAO.getFundingVideoReportsCount(search, order);
    }

//    영상 신고 상태 업데이트
    @Override
    public void updateVideoFundingReportStatus(Long fundingId, String reportStatus){
        fundingReportDAO.updateVideoFundingReportStatus(fundingId, reportStatus);
    }

    //    글 펀딩 신고 목록조회
    @Override
    public List<FundingReportDTO> getFundingTextReports(String search, String order, AdminPagination pagination){
        return fundingReportDAO.getFundingTextReports(search, order, pagination);
    }

    //    글 펀딩 신고 전체조회
    @Override
    public int getFundingTextReportsCount(String search, String order){
        return fundingReportDAO.getFundingTextReportsCount(search, order);
    }

    //    글 신고 상태 업데이트
    @Override
    public void updateTextFundingReportStatus(Long fundingId, String reportStatus){
        fundingReportDAO.updateTextFundingReportStatus(fundingId, reportStatus);
    }
}
