package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.search.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FundingMapper {
//    내 펀딩 게시물 전체 조회
    public List<FundingDTO> selectByMemberId(@Param("workAndFundingPagination") WorkAndFundingPagination workAndFundingPagination
            , @Param("memberId") Long memberId, @Param("postType") String postType);

//    내 펀딩 게시물 전체 갯수
    public int selectCount(@Param("memberId") Long memberId, @Param("postType") String postType);

//    펀딩 정보 조회
    public Optional<FundingDTO> selectById(@Param("id") Long id, @Param("postType") String postType);








    // 파일 ID를 기준으로 펀딩 상품 삭제
    void deleteFundingProductById(@Param("id") Long id);

    // 펀딩 삽입
    public void insert(FundingDTO fundingDTO);

    // 펀딩 상품 저장
    public void insertFundingProduct(FundingProductVO fundingProductVO);

    // 작품 목록 조회 및 썸네일 불러오기 (검색 및 필터링 추가)
    public List<FundingDTO> selectFundingList(
            @Param("search") Search search,
            @Param("pagination") Pagination pagination
    );

    // 검색 조건이 포함된 총 펀딩 수 조회
    public int selectTotalWithSearchAndType(@Param("search") Search search);

    // 펀딩 정보 수정 (tbl_funding 및 tbl_post 업데이트)
    public void updateFunding(FundingDTO fundingDTO);
    public void updatePost(FundingDTO fundingDTO);

    // 썸네일 파일 ID 업데이트 메소드
    void updateThumbnailFileId(@Param("fundingId") Long fundingId, @Param("thumbnailFileId") Long thumbnailFileId);

    // 펀딩 상태 갱신 (펀딩 중 -> 펀딩 종료)
    public void updateFundingStatusToEnded();

    // 펀딩 ID로 작품 조회
    FundingDTO selectByFundingId(Long id);

    // 상품 정보 수정
    public void updateFundingProduct(FundingProductVO fundingProductVO);

    // 펀딩 상세 조회
    public List<FundingDTO> selectFundingById(@Param("id") Long id);

    // 다중 파일 조회 (작품 상세보기)
    public List<PostFileDTO> selectFilesByPostId(@Param("postId") Long postId);

    // 펀딩 상품 정보 조회
    public List<FundingProductVO> selectFundingProductsByFundingId(@Param("fundingId") Long fundingId);

    // 같은 장르의 펀딩 게시글 조회 (최대 3개)
    public List<FundingDTO> selectRelatedFundingByGenre(
            @Param("genreType") String genreType,
            @Param("fundingId") Long fundingId
    );
}
