package com.app.ggumteo.mapper.report;
import com.app.ggumteo.domain.report.WorkReportDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface WorkReportMapper {
    List<WorkReportDTO> selectVideoReports(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );

    int countVideoReports(
            @Param("search") String search,
            @Param("order") String order
    );
}
