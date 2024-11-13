package com.app.ggumteo.mapper;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.buy.BuyFundingProductDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.buy.BuyFundingProductMapper;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j

public class FundingMapperTests {
    @Autowired
    private FundingMapper fundingMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private BuyFundingProductMapper buyFundingProductMapper;

    @Test
    public void testSelectByMemberId() {

//        List<FundingDTO> fundingPosts = fundingMapper.selectByMemberId(1L);
////        fundingMapper.selectByMemberId(1L).stream().map(FundingDTO::toString).forEach(log::info);
//
//        for (FundingDTO fundingDTO : fundingPosts) {
//            log.info("{}", fundingDTO);
//        }
    }

    @Test
    public void testSelectBuyerByFundingPostId() {
        FundingDTO fundingDTO = null;
        SettingTablePagination settingTablePagination = new SettingTablePagination();
        fundingDTO = fundingMapper.selectById(9L, PostType.FUNDINGVIDEO.name()).get();
        settingTablePagination.setTotal(buyFundingProductMapper.selectCount(fundingDTO.getId()));
        settingTablePagination.progress();
        buyFundingProductMapper.selectByFundingPostId(
                        settingTablePagination, fundingDTO.getId()).stream()
                .map(BuyFundingProductDTO::toString).forEach(log::info);
    }

    @Test
    public void testInsertFunding() {
        // tbl_post에 데이터 삽입
        PostVO postVO = new PostVO();
        postVO.setPostTitle("펀딩 테스트 제목");
        postVO.setPostContent("펀딩 테스트 글 내용");
        postVO.setPostType("FUNDINGVIDEO");  // PostType에 맞게 설정
        postVO.setMemberProfileId(1L);  // 회원 ID
        postMapper.insert(postVO);  // tbl_post에 데이터 삽입

        // Post ID가 제대로 설정되었는지 확인
        Long postId = postVO.getId();
        log.info("삽입된 Post ID: {}", postId);

        // tbl_funding에 데이터 삽입 (postId를 id로 사용)
        FundingDTO fundingDTO = new FundingDTO();
        fundingDTO.setId(postId);  // tbl_funding의 id를 tbl_post의 id로 설정
        fundingDTO.setGenreType("comedy");
        fundingDTO.setInvestorNumber(100);
        fundingDTO.setTargetPrice(500000);
        fundingDTO.setConvergePrice(10000);
        fundingDTO.setFundingContent("펀딩 상세 설명");
        fundingDTO.setFundingStatus("펀딩 중");

        // FundingMapper를 통해 데이터베이스에 저장
        fundingMapper.insert(fundingDTO);
        log.info("펀딩 데이터 삽입 완료: {}", fundingDTO);

        // 펀딩 상품 데이터 삽입
        FundingProductVO product1 = new FundingProductVO();
        product1.setProductName("상품 1");
        product1.setProductPrice(10000);
        product1.setProductAmount(50);
        product1.setFundingId(postId);

        FundingProductVO product2 = new FundingProductVO();
        product2.setProductName("상품 2");
        product2.setProductPrice(20000);
        product2.setProductAmount(30);
        product2.setFundingId(postId);

        // 상품을 저장하는 Mapper 호출
        fundingMapper.insertFundingProduct(product1);
        fundingMapper.insertFundingProduct(product2);

        log.info("펀딩 및 펀딩 상품이 성공적으로 삽입되었습니다.");

        // 삽입된 펀딩과 상품을 조회하여 검증
        Optional<FundingDTO> insertedFunding = fundingMapper.selectById(postId, "FUNDINGVIDEO");
        FundingDTO fetchedFunding = insertedFunding.get();
        log.info("삽입된 펀딩 조회: {}", fetchedFunding);
        List<FundingProductVO> products = fundingMapper.selectFundingProductsByFundingId(postId);
        products.forEach(product -> log.info("펀딩 상품: {}", product));
    }

    @Test
    public void testSelectFundingList() {
        // 검색 및 페이지네이션 설정
        Search search = new Search();
        search.setKeyword(""); // 검색 키워드를 설정 (예: "테스트" 등). 검색 키워드가 없으면 전체 조회.
        search.setGenreType(""); // 장르 필터를 설정 (예: "comedy"). 필터가 없으면 전체 조회.
        search.setPostType("FUNDINGVIDEO"); // 포스트 타입 설정 (예: "TEXT", "VIDEO")

        Pagination pagination = new Pagination();
        pagination.setPage(1); // 첫 번째 페이지
        pagination.progress2(); // 페이지네이션 계산을 위한 progress2 호출

        // FundingMapper를 통해 목록 조회
        List<FundingDTO> fundingList = fundingMapper.selectFundingList(search, pagination);

        // 조회된 펀딩 목록 출력
        if (fundingList.isEmpty()) {
            log.info("조회된 펀딩 목록이 없습니다.");
        } else {
            fundingList.forEach(fundingDTO -> log.info("펀딩 목록: {}", fundingDTO));
        }
    }

    @Test
    public void testDeleteFundingProductById() {
        Long productId = 10L; // 삭제할 펀딩 상품 ID (실제 테스트 전에 존재하는 ID로 설정)

        // 펀딩 상품 삭제
        fundingMapper.deleteFundingProductById(productId);
        log.info("펀딩 상품 ID {}가 '상품 삭제' 상태로 변경되었습니다.", productId);

        // 삭제 상태 확인
        FundingProductVO deletedProduct = fundingMapper.selectFundingProductsByFundingId(1L).stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (deletedProduct != null) {
            log.info("삭제된 펀딩 상품 상태: {}", deletedProduct.getStatus());
        } else {
            log.info("펀딩 상품 ID {}를 찾을 수 없습니다.", productId);
        }
    }

    @Test
    public void testSelectFundingById() {
        Long fundingId = 9L; // 테스트할 펀딩 ID (실제 테스트 전에 존재하는 ID로 설정)

        FundingDTO fundingDTO = fundingMapper.selectFundingById(fundingId);
        if (fundingDTO != null) {
            log.info("펀딩 상세 조회: {}", fundingDTO);
        } else {
            log.info("펀딩 ID {}에 해당하는 데이터를 찾을 수 없습니다.", fundingId);
        }
    }
    @Test
    public void testSelectFilesByPostId() {
        Long postId = 9L; // 테스트할 포스트 ID (실제 테스트 전에 존재하는 ID로 설정)

        List<PostFileDTO> files = fundingMapper.selectFilesByPostId(postId);
        if (files.isEmpty()) {
            log.info("포스트 ID {}에 해당하는 파일이 없습니다.", postId);
        } else {
            files.forEach(file -> log.info("파일 정보: {}", file));
        }
    }

    @Test
    public void testSelectRelatedFundingByGenre() {
        String genreType = "comedy"; // 테스트할 장르
        Long currentFundingId = 9L; // 현재 조회 중인 펀딩 ID (실제 테스트 전에 존재하는 ID로 설정)

        List<FundingDTO> relatedFundings = fundingMapper.selectRelatedFundingByGenre(genreType, currentFundingId);
        if (relatedFundings.isEmpty()) {
            log.info("장르 '{}'에 해당하는 관련 펀딩 게시글이 없습니다.", genreType);
        } else {
            relatedFundings.forEach(funding -> log.info("관련 펀딩 게시글: {}", funding));
        }
    }

    @Test
    public void testUpdateThumbnailFileId() {
        Long fundingId = 9L; // 테스트할 펀딩 ID (실제 테스트 전에 존재하는 ID로 설정)
        Long newThumbnailFileId = 5L; // 새로운 썸네일 파일 ID (실제 테스트 전에 존재하는 ID로 설정)

        // 썸네일 파일 ID 업데이트
        fundingMapper.updateThumbnailFileId(fundingId, newThumbnailFileId);
        log.info("펀딩 ID {}의 썸네일 파일 ID가 {}로 업데이트되었습니다.", fundingId, newThumbnailFileId);

        // 업데이트 확인
        FundingDTO updatedFunding = fundingMapper.selectByFundingId(fundingId);
        if (updatedFunding != null) {
            log.info("업데이트된 썸네일 파일 ID: {}", updatedFunding.getThumbnailFileId());
        } else {
            log.info("펀딩 ID {}에 해당하는 데이터를 찾을 수 없습니다.", fundingId);
        }
    }
    @Test
    public void testUpdateFundingStatusToEnded() {
        // 펀딩 상태 갱신 실행
        fundingMapper.updateFundingStatusToEnded();
        log.info("현재 '펀딩 중' 상태인 모든 펀딩의 상태가 '펀딩 종료'로 변경되었습니다.");

        // 갱신된 상태 확인 (예: 특정 펀딩 ID를 조회하여 상태 확인)
        Long fundingId = 9L; // 테스트할 펀딩 ID (실제 테스트 전에 존재하는 ID로 설정)
        FundingDTO fundingDTO = fundingMapper.selectByFundingId(fundingId);
        if (fundingDTO != null) {
            log.info("펀딩 ID {}의 현재 상태: {}", fundingId, fundingDTO.getFundingStatus());
        } else {
            log.info("펀딩 ID {}에 해당하는 데이터를 찾을 수 없습니다.", fundingId);
        }
    }
    @Test
    public void testSelectFundingListAndTotal() {
        // 검색 및 페이지네이션 설정
        Search search = new Search();
        search.setKeyword("테스트"); // 예: "테스트" 키워드로 검색
        search.setGenreType("comedy"); // 예: "comedy" 장르로 필터링
        search.setPostType("FUNDINGVIDEO"); // 포스트 타입 설정

        Pagination pagination = new Pagination();
        pagination.setPage(1); // 첫 번째 페이지
        pagination.progress2(); // 페이지네이션 계산을 위한 progress2 호출

        // 펀딩 목록 조회
        List<FundingDTO> fundingList = fundingMapper.selectFundingList(search, pagination);
        if (fundingList.isEmpty()) {
            log.info("검색 조건에 해당하는 펀딩 목록이 없습니다.");
        } else {
            fundingList.forEach(funding -> log.info("펀딩 목록: {}", funding));
        }

        // 총 펀딩 수 조회
        int totalFundings = fundingMapper.selectTotalWithSearchAndType(search);
        log.info("검색 조건에 해당하는 총 펀딩 수: {}", totalFundings);
    }


}

