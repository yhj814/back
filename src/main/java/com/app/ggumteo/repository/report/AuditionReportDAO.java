package com.app.ggumteo.repository.report;

import com.app.ggumteo.domain.report.AuditionReportDTO;
import com.app.ggumteo.mapper.report.AuditionReportMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditionReportDAO {
    private final AuditionReportMapper auditionReportMapper;

//    영상 모집글 신고 목록
    public List<AuditionReportDTO> getVideoAuditionReports(String search, String order, AdminPagination pagination) {
        return auditionReportMapper.selectVideoAuditionReports(search, order, pagination);
    }

//    영상 모집글 신고목록 카운트
     public int getVideoAuditionReportCount(String search, String order) {
        return auditionReportMapper.countVideoAuditionReports(search,order);
     }

//    영상 모집글 신고상태 변경
    public void updateVideoAuditionReportStatus(Long auditionId, String reportStatus){
        auditionReportMapper.videoAuditionStatusChange(auditionId,reportStatus);
    }
}
