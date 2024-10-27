package com.app.ggumteo.mapper.work;

import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkMapper {

    // 작품 삽입
    void insert(WorkVO workVO);

    // 총 작품 수 조회
    int selectTotal();

    // 작품 ID로 작품 조회
    WorkDTO selectById(Long id);

    // 조회수 증가
    void increaseReadCount(Long id);

    // 작품 정보 수정 (tbl_work 및 tbl_post 업데이트)
    void updateWork(WorkDTO workDTO);

    // 작품 삭제
    void deleteById(Long id);

    // 게시물 삭제
    void deletePostById(Long id);

    // 작품 목록 조회 및 썸네일 불러오기 (장르 필터 추가)
    List<WorkDTO> selectAllWithThumbnail(@Param("genreType") String genreType);

    // 검색 조건이 포함된 총 작품 수 조회
    int selectTotalWithSearch(@Param("workSearch") WorkDTO workSearch);

    // 상세보기에서 다중 파일 조회
    List<PostFileDTO> selectFilesByPostId(Long postId);
}
