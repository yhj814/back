package com.app.ggumteo.mapper.admin;

import com.app.ggumteo.domain.admin.AnnouncementVO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
//    공지사항 작성
    public void insert(AnnouncementVO announcementVO);

//    공지사항 전체 조회 (페이지네이션, 정렬, 검색 포함)
    List<AnnouncementVO> selectAll(@Param("pagination") AdminPagination pagination,
                                   @Param("order") String order,
                                   @Param("search") String search);

//    총 공지사항 수 조회 (검색 포함)
    int countTotal(@Param("search") String search);

//    공지사항 수정
    void updateAnnouncement(@Param("announcement") AnnouncementVO announcementVO);

//     공지사항 삭제 (체크박스 있어서 ids로 받음)
    void deleteAnnouncements(@Param("ids") List<Integer> ids);


}
