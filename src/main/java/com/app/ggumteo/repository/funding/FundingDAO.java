package com.app.ggumteo.repository.funding;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FundingDAO {
    private final FundingMapper fundingMapper;

//    내 펀딩 게시물 전체 조회
    public List<FundingDTO> findByMemberId(MyWorkAndFundingPagination myWorkAndFundingPagination, Long memberId, String postType) {
        return fundingMapper.selectByMemberId(myWorkAndFundingPagination, memberId, postType);
    }

//    내 펀딩 게시물 전체 개수
    public int getTotal(Long memberId, String postType){
        return fundingMapper.selectCount(memberId, postType);
    }

//    펀딩 정보 조회
    public Optional<FundingDTO> findById(Long id, String postType) {
        return fundingMapper.selectById(id, postType);
    }







    // 펀딩 삽입 메서드
    public void save(FundingDTO fundingDTO) {
        if (fundingDTO.getFundingStatus() == null) {
            fundingDTO.setFundingStatus("펀딩 중");
        }
        fundingMapper.insert(fundingDTO);
    }


    // 펀딩 상품 삽입 메서드
    public void saveFundingProduct(FundingProductVO fundingProductVO) {
        fundingMapper.insertFundingProduct(fundingProductVO);
    }

    // 작품 목록 조회 (썸네일 포함, 검색 및 필터링 추가)
    public List<FundingDTO> findAllFundingList(Search search, Pagination pagination) {
        return fundingMapper.selectFundingList(search, pagination);
    }

    // 검색 조건이 포함된 총 펀딩 수 조회
    public int findTotalWithSearchAndType(Search search) {
        return fundingMapper.selectTotalWithSearchAndType(search);
    }

    // 펀딩 정보 수정
    public void updateFunding(FundingDTO fundingDTO) {
        fundingMapper.updateFunding(fundingDTO);
    }
    public void updatePost(FundingDTO fundingDTO) {
        fundingMapper.updatePost(fundingDTO);
    }

    // 펀딩 상태 갱신 (펀딩 중 -> 펀딩 종료)
    public void updateFundingStatusToEnded() {
        fundingMapper.updateFundingStatusToEnded();
    }
    // 펀딩 상세보기
    public FundingDTO findByFundingId(Long id) {
        return fundingMapper.selectFundingById(id);
    }

    // 다중 파일 조회 (작품 상세보기)
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return fundingMapper.selectFilesByPostId(postId);
    }

    // 펀딩 상품 정보 조회
    public List<FundingProductVO> findFundingProductsByFundingId(Long fundingId) {
        List<FundingProductVO> fundingProducts = fundingMapper.selectFundingProductsByFundingId(fundingId);
        log.info("Found products for Funding ID {}: {}", fundingId, fundingProducts);
        return fundingProducts;
    }

    public void updateFundingProduct(FundingProductVO fundingProductVO) {
        fundingMapper.updateFundingProduct(fundingProductVO);
    }

    // 썸네일 파일 ID 업데이트 메소드 추가
    public void updateThumbnailFileId(Long fundingId, Long thumbnailFileId) {
        fundingMapper.updateThumbnailFileId(fundingId, thumbnailFileId);
    }

    // 같은 장르의 펀딩 게시글 조회
    public List<FundingDTO> findRelatedFundingByGenre(String genreType, Long fundingId) {
        return fundingMapper.selectRelatedFundingByGenre(genreType, fundingId);
    }

    // 펀딩 ID로 조회
    public FundingDTO findFundingId(Long id) {
        return fundingMapper.selectByFundingId(id);
    }

    // 파일 ID를 기준으로 펀딩 상품 삭제
    public void deleteFundingProductById(Long id) {
        fundingMapper.deleteFundingProductById(id);
    }


//    메인페이지
public List<FundingDTO> selectTopTextFundingForMainPage() {
    return fundingMapper.selectTopTextFundingForMainPage();
}

    public List<FundingDTO> selectTopVideoFundingForMainPage() {
        return fundingMapper.selectTopVideoFundingForMainPage();
    }

}


