package com.app.ggumteo.mapper.report;

import com.app.ggumteo.domain.report.ReplyReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyReportMapper {

    // 영상 댓글 신고 목록
    List<ReplyReportDTO> selectVideoReplyReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

    // 총 영상 댓글 신고 목록 카운트
    int countVideoReplyReports(
            @Param("search") String search,
            @Param("order") String order
    );

    // 영상 댓글 신고 상태 업데이트
    void videoReplyStatusChange(
            @Param("replyId") Long replyId,
            @Param("reportStatus") String reportStatus
    );

    // 글 댓글 신고 목록
    List<ReplyReportDTO> selectTextReplyReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

    // 총 글 댓글 신고 목록 카운트
    int countTextReplyReports(
            @Param("search") String search,
            @Param("order") String order
    );

    // 글 댓글 신고 상태 업데이트
    void textReplyStatusChange(
            @Param("replyId") Long replyId,
            @Param("reportStatus") String reportStatus
    );
}

