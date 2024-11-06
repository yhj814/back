package com.app.ggumteo.mapper;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.funding.BuyFundingProductMapper;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.pagination.MyPagePagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        fundingDTO = fundingMapper.selectById(9L, PostType.VIDEO.name()).get();
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
        postVO.setPostType("TEXT");  // 글 유형
        postVO.setMemberProfileId(1L);  // 회원 ID
        postMapper.insert(postVO);  // tbl_post에 데이터 삽입

        // Post ID가 제대로 설정되었는지 확인
        Long postId = postVO.getId();
        if (postId == null) {
            log.info("postId가 없습니다!!!!!!!!!!!!!!!!!");
        }

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

        // 펀딩 상품 데이터 삽입
        FundingProductVO product1 = new FundingProductVO();
        product1.setProductName("상품 1");
        product1.setProductPrice("10000");
        product1.setProductAmount("50");
        product1.setFundingId(postId);

        FundingProductVO product2 = new FundingProductVO();
        product2.setProductName("상품 2");
        product2.setProductPrice("20000");
        product2.setProductAmount("30");
        product2.setFundingId(postId);

        // 상품을 저장하는 Mapper 호출
        fundingMapper.insertFundingProduct(product1);
        fundingMapper.insertFundingProduct(product2);

        log.info("펀딩 및 펀딩 상품이 성공적으로 삽입되었습니다.");
    }
    @Test
    public void testSelectFundingList() {
        // 검색 및 페이지네이션 설정
        Search search = new Search();
        search.setKeyword(""); // 검색 키워드를 설정 (예: "테스트" 등). 검색 키워드가 없으면 전체 조회.
        search.setGenreType(""); // 장르 필터를 설정 (예: "comedy"). 필터가 없으면 전체 조회.
        search.setPostType("TEXT"); // 포스트 타입 설정 (예: "TEXT", "VIDEO")

        Pagination pagination = new Pagination();
        pagination.setPage(1); // 첫 번째 페이지
        pagination.progress2(); // 페이지네이션 계산을 위한 progress2 호출

        // FundingMapper를 통해 목록 조회
        List<FundingDTO> fundingList = fundingMapper.selectFundingList(search, pagination);

        // 조회된 펀딩 목록 출력
        if (fundingList.isEmpty()) {
            log.info("조회된 펀딩 목록이 없습니다.");
        } else {
            fundingList.stream()
                    .map(FundingDTO::toString)
                    .forEach(log::info);
        }
    }




}

