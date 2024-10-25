package com.app.ggumteo.repository.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.mapper.funding.FundingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FundingDAO {
    private final FundingMapper fundingMapper;

//    내 게시물 조회
    public List<FundingDTO> findByMemberId(Long memberId) {
        return fundingMapper.selectByMemberId(memberId);
    }
}
