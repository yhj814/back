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
import com.app.ggumteo.pagination.SettingTablePagination;
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

//    @Test
//    public void testUpdateFunding() {
//        // 기존 펀딩 정보를 조회
//        Long fundingId = 1L; // 수정할 펀딩 ID 설정
//        FundingDTO fundingDTO = fundingMapper.selectById(fundingId);
//
//        if (fundingDTO == null) {
//            log.info("펀딩 정보를 찾을 수 없습니다. ID: {}", fundingId);
//            return;
//        }
//
//        // 수정할 내용 설정
//        fundingDTO.setGenreType("action");
//        fundingDTO.setInvestorNumber(150);
//        fundingDTO.setTargetPrice(600000);
//        fundingDTO.setConvergePrice(300000);
//        fundingDTO.setFundingContent("수정된 펀딩 상세 설명");
//        fundingDTO.setFundingStatus("펀딩 완료");
//
//        // Mapper를 통해 업데이트
//        fundingMapper.updateFunding(fundingDTO);
//
//        log.info("펀딩 정보가 성공적으로 수정되었습니다. ID: {}", fundingId);
//    }
}

