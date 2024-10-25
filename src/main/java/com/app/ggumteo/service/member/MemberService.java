package com.app.ggumteo.service.member;

import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;

import java.util.Optional;

public interface MemberService {
    public void join(MemberVO memberVO);

    public Optional<MemberVO> getKakaoMember(String memberEmail);



}
