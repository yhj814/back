package com.app.ggumteo.service;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.service.funding.FundingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j

public class FundingServiceTests {
    @Autowired
    private FundingService fundingService;

    @Test
    public void getMyFundingPosts() {
        List<FundingDTO> fundingPosts = fundingService.getMemberFundingPosts(1L);
//        fundingMapper.selectByMemberId(1L).stream().map(FundingDTO::toString).forEach(log::info);

        for (FundingDTO fundingDTO : fundingPosts) {
            log.info("{}", fundingDTO);
        }
    }
}
