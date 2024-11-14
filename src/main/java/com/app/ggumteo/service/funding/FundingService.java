package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FundingService {
    // 펀딩 작성
    void write(FundingDTO fundingDTO);

    List<FundingDTO> findFundingList(Search search, Pagination pagination);

    List<PostFileDTO> findFilesByPostId(Long postId);

    void updateFunding(FundingDTO fundingDTO, List<Long> deletedFileIds);

    // 펀딩 상품 정보 조회 메서드
    List<FundingProductVO> findFundingProductsByFundingId(Long fundingId);

    int findTotalWithSearchAndType(Search search);
// 상품 수정
    void updateFundingProduct(FundingProductVO fundingProductVO);

    // 상세보기 기본정보 조회
    FundingDTO findFundingById(Long id);

    void updateFundingStatusToEnded();

    // 같은 장르의 펀딩 게시글 조회 (최대 5개)
    List<FundingDTO> findRelatedFundingByGenre(String genreType, Long fundingId);

    void buyFundingProduct(Long fundingProductId, Long memberId, Long fundingId, int productPrice);

    FundingDTO findFundingId(Long id);


    List<FundingDTO> getTopTextFundingForMainPage();


    List<FundingDTO> getTopVideoFundingForMainPage();

}
