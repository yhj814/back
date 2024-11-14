package com.app.ggumteo.mapper.work;

import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.search.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface WorkMapper {

    // 작품 삽입
    void insert(WorkVO workVO);

    // 총 작품 수 조회
    int selectTotalWithGenreType(@Param("genreType") String genreType);

    // 작품 ID로 작품 조회
    WorkDTO selectById(Long id);

    // 작품 id로 작품 조회(댓글 알람용)
    WorkDTO selectWorkById(@Param("id") Long id);

    // 조회수 증가
    void increaseReadCount(Long id);

    // 작품 정보 수정 (tbl_work 및 tbl_post 업데이트)
    void updateWork(WorkDTO workDTO);
    void updatePost(WorkDTO workDTO);

    // 작품 삭제
    void deleteById(Long id);

    // 게시물 삭제
    void deletePostById(Long id);

    // 작품 목록 조회 및 썸네일 불러오기 (검색 및 필터링 추가)
    List<WorkDTO> selectAllWithThumbnailAndSearchAndType(@Param("search") Search search, @Param("pagination") Pagination pagination);

    // 검색 조건이 포함된 총 작품 수 조회
    int selectTotalWithSearchAndType(@Param("search") Search search);

    // 상세보기에서 다중 파일 조회
    List<PostFileDTO> selectFilesByPostId(@Param("postId") Long postId);

    // 같은 장르의 최신순 3개 작품 조회
    List<WorkDTO> selectThreeByGenre(
            @Param("genreType") String genreType,
            @Param("workId") Long workId,
            @Param("postType") String postType
    );

    // 작가의 다른 최신 작품 3개 조회
    List<WorkDTO> selectThreeByAuthor(
            @Param("memberProfileId") Long memberProfileId,
            @Param("workId") Long workId,
            @Param("postType") String postType
    );

    // 썸네일 파일 ID 업데이트 메소드
    void updateThumbnailFileId(@Param("workId") Long workId, @Param("thumbnailFileId") Long thumbnailFileId);

//************ 마이페이지 **************

    // 내 작품 게시물 전체 조회
    public List<WorkDTO> selectByMemberId(@Param("myWorkAndFundingPagination") MyWorkAndFundingPagination myWorkAndFundingPagination
            , @Param("memberId") Long memberId, @Param("postType") String postType);

    // 내 작품 게시물 전체 갯수
    public int selectCount(@Param("memberId") Long memberId, @Param("postType") String postType);

    // 작품 정보 조회
    public Optional<WorkDTO> selectByIdAndPostType(@Param("id") Long id, @Param("postType") String postType);





    //메인 페이지
    WorkDTO selectMostReadTextWorkForMainPage();
    WorkDTO selectMostReadVideoWorkForMainPage();

}
