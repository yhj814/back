package com.app.ggumteo.service.myPage;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;

import java.util.List;
import java.util.Optional;

public interface MyPageService {
    //    회원 정보 조회
    public Optional<MemberVO> getMember(Long Id);
    //    내 게시물 조회
    public List<FundingDTO> getMyFundingList(Long memberId);
}
