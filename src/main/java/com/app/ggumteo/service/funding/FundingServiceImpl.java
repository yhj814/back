package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.repository.funding.FundingDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
@Transactional(rollbackFor = Exception.class)
public class FundingServiceImpl implements FundingService {
    private final FundingDAO fundingDAO;

    @Override
    public List<FundingDTO> getMyFundingPosts(Long memberId) {
        return fundingDAO.findByMemberId(memberId);
    }
}
