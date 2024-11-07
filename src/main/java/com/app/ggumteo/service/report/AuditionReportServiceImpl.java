package com.app.ggumteo.service.report;

import com.app.ggumteo.domain.report.AuditionReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.report.AuditionReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuditionReportServiceImpl implements AuditionReportService {
    private final AuditionReportDAO auditionReportDAO;

//    영상 모집글 신고 목록
    @Override
    public List<AuditionReportDTO> getVideoAuditionReports(String search, String order, AdminPagination pagination){
        return auditionReportDAO.getVideoAuditionReports(search, order, pagination);
    }

//    영상 모집글 신고 목록 카운트
    @Override
    public int getVideoAuditionReportCount(String search, String order){
        return auditionReportDAO.getVideoAuditionReportCount(search, order);
    }

//    영상 모집글 신고 상태 업데이트
    @Override
    public void updateVideoAuditionReportStatus(Long auditionId, String reportStatus){
        auditionReportDAO.updateVideoAuditionReportStatus(auditionId, reportStatus);
    }
}
