package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.FundingMapper;
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
        FundingDTO fundingDTO = new FundingDTO();
        fundingDTO.setMemberId(1L);

        List<FundingDTO> fundingPosts = fundingMapper.selectByMemberId(fundingDTO.getMemberId());
        fundingPosts.stream().map(FundingDTO::toString).forEach(log::info);
    }
}
