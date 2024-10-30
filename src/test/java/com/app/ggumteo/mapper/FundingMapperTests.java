package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.pagination.MyPagePagination;
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

    @Test
    public void testSelectByMemberId() {

//        List<FundingDTO> fundingPosts = fundingMapper.selectByMemberId(1L);
////        fundingMapper.selectByMemberId(1L).stream().map(FundingDTO::toString).forEach(log::info);
//
//        for (FundingDTO fundingDTO : fundingPosts) {
//            log.info("{}", fundingDTO);
//        }
    }
}
