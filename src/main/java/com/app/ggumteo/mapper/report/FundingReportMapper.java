package com.app.ggumteo.mapper.report;

import com.app.ggumteo.domain.report.FundingReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundingReportMapper {
//    영상 펀딩 신고목록 조회
    List<FundingReportDTO> selectFundingVideoReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

//    총 영상 펀딩 신고 목록들
    int fundingVideoReportCounts(
            @Param("search") String search,
            @Param("order") String order
    );

//    영상 펀딩 신고 상태 업데이트
    void fundingVideoStatusChange(
            @Param("fundingId") Long fundingId,
            @Param("reportStatus") String reportStatus
    );

    //    글 펀딩 신고목록 조회
    List<FundingReportDTO> selectFundingTextReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

    //    글 영상 펀딩 신고 목록들
    int fundingTextReportCounts(
            @Param("search") String search,
            @Param("order") String order
    );

    //    글 펀딩 신고 상태 업데이트
    void fundingTextStatusChange(
            @Param("fundingId") Long fundingId,
            @Param("reportStatus") String reportStatus
    );
}
