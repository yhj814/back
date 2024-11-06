package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
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






    // 펀딩 삽입
    public void insert(FundingDTO fundingDTO);
    // 펀딩 상품 저장
    void insertFundingProduct(FundingProductVO fundingProductVO);

    // 작품 목록 조회 및 썸네일 불러오기 (검색 및 필터링 추가)
    List<FundingDTO> selectFundingList(@Param("search") Search search, @Param("pagination") Pagination pagination);

    // 펀딩 상세 조회 메소드
    List<FundingDTO> selectFundingById(@Param("id") Long id);

    // 펀딩 상품 정보 조회
    List<FundingProductVO> selectFundingProductsByFundingId(@Param("fundingId") Long fundingId);

    // 상세보기에서 다중 파일 조회
    List<PostFileDTO> selectFilesByPostId(@Param("postId") Long postId);

    // 검색 조건이 포함된 총 펀딩 수 조회
    int selectTotalWithSearchAndType(@Param("search") Search search);

    // 펀딩 정보 수정 (tbl_funding 및 tbl_post 업데이트)
    void updateFunding(FundingDTO fundingDTO);

    public void updateFundingStatusToEnded();


        // 같은 장르의 펀딩 게시글 조회 
        List<FundingDTO> selectRelatedFundingByGenre(@Param("genreType") String genreType, @Param("fundingId") Long fundingId);


}
