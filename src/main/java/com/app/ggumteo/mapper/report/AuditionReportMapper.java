package com.app.ggumteo.mapper.report;

import com.app.ggumteo.domain.report.AuditionReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditionReportMapper {

//    영상 모집글 신고 목록
    List<AuditionReportDTO> selectVideoAuditionReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

//    영상 모집글 신고 목록 카운트
    int countVideoAuditionReports(
            @Param("search") String search,
            @Param("order") String order
    );

//    영상 모집글 신고상태 변경
    void videoAuditionStatusChange(
            @Param("auditionId") Long auditionId,
            @Param("reportStatus") String reportStatus
    );
}
