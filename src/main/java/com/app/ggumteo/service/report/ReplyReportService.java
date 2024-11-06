package com.app.ggumteo.service.report;


import com.app.ggumteo.domain.report.ReplyReportDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface ReplyReportService {

//    영상 댓글 신고 목록
    List<ReplyReportDTO> getVideoReplyReports(String search, String order, AdminPagination pagination);

//    영상 댓글 신고 목록 전체조회
    int getVideoReplyReportsCount(String search, String order);

//    영상 댓글 신고 상태 업데이트
    void updateReplyReportStatus(Long replyId, String reportStatus);
}
