package com.app.ggumteo.service.myPage;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.repository.funding.FundingDAO;
import com.app.ggumteo.repository.member.MemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Primary
@Transactional(rollbackFor = Exception.class)
public class MyPageServiceImpl implements MyPageService {
    private final MemberDAO memberDAO;
    private final FundingDAO fundingDAO;


    @Override
    public Optional<MemberVO> getMember(Long Id) {
        return memberDAO.findById(Id);
    }

    @Override
    public List<FundingDTO> getMyFundingList(Long memberId) {
        return fundingDAO.findByMemberId(memberId);
    }
}
