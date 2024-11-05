package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
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

    // 펀딩 정보 수정 (tbl_funding 및 tbl_post 업데이트)
    void updateFunding(FundingDTO fundingDTO);

    public void updateFundingStatusToEnded();

}
