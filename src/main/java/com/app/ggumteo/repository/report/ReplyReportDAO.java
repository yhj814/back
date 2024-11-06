package com.app.ggumteo.repository.report;

import com.app.ggumteo.domain.report.ReplyReportDTO;
import com.app.ggumteo.mapper.report.ReplyReportMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyReportDAO {
    private final ReplyReportMapper replyReportMapper;

//    영상 댓글 신고 목록
    public List<ReplyReportDTO> getVideoReplyReports(String search, String order, AdminPagination pagination) {
        return replyReportMapper.selectVideoReplyReports(search, order, pagination);
    }

//    영상 댓글 신고 목록 전체조회
    public int getVideoReplyReportsCount(String search, String order) {
        return replyReportMapper.countVideoReplyReports(search,order);
    }

//    영상 댓글 신고 상태 업데이트
    public void updateReplyReportStatus(Long replyId, String reportStatus) {
        replyReportMapper.videoReplyStatusChange(replyId, reportStatus);
    }

}
