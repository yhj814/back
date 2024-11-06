package com.app.ggumteo.mapper.report;
import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface WorkReportMapper {
//    영상 신고 목록
    List<WorkReportDTO> selectVideoReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

//    총 영상 신고 목록들
    int countVideoReports(
            @Param("search") String search,
            @Param("order") String order
    );

//    영상 신고 상태 업데이트
    void videoStatusChange(
            @Param("workId") Long workId,
            @Param("reportStatus") String reportStatus
    );

//    글 신고 목록
    List<WorkReportDTO> selectTextReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

//    총 글 신고 목록들
    int countTextReports(
            @Param("search") String search,
            @Param("order") String order
    );

//    글 신고 상태 업데이트
    void textStatusChange(
            @Param("workId") Long workId,
            @Param("reportStatus") String reportStatus
    );

}
