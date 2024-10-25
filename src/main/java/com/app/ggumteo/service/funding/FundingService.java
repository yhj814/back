package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.funding.FundingDTO;

import java.util.List;

public interface FundingService {
    //    내 게시물 조회
    public List<FundingDTO> getMemberFundingPosts(Long memberId);
}
