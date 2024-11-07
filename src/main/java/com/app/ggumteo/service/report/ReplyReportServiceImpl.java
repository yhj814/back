package com.app.ggumteo.service.report;

import com.app.ggumteo.domain.report.ReplyReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.report.ReplyReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ReplyReportServiceImpl implements ReplyReportService {
    private final ReplyReportDAO replyReportDAO;

//    영상 댓글 신고 목록 조회
    @Override
    public List<ReplyReportDTO> getVideoReplyReports(String search, String order, AdminPagination pagination){
        return replyReportDAO.getVideoReplyReports(search, order, pagination);
    }

//    영상 댓글 신고 목록 전체조회
    @Override
    public int getVideoReplyReportsCount(String search, String order){
        return replyReportDAO.getVideoReplyReportsCount(search, order);
    }

//    영상 댓글 신고 상태 업데이트
    @Override
    public void updateReplyReportStatus(Long replyId, String reportStatus){
        replyReportDAO.updateReplyReportStatus(replyId, reportStatus);
    }

//    글 댓글 신고 목록 조회
    @Override
    public List<ReplyReportDTO> getTextReplyReports(String search, String order, AdminPagination pagination){
        return replyReportDAO.getTextReplyReports(search, order, pagination);
    }

//    글 댓글 신고 목록 전체조회
    @Override
    public int getTextReplyReportsCount(String search, String order){
        return replyReportDAO.getTextReplyReportsCount(search, order);
    }

//    글 댓글 신고 상태 업데이트
    @Override
    public void updateTextReplyReportStatus(Long replyId, String reportStatus){
        replyReportDAO.updateTextReplyReportStatus(replyId, reportStatus);
    }
}
