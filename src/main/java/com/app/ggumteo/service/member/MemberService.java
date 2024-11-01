package com.app.ggumteo.service.member;

import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;

import java.util.Optional;

public interface MemberService {
    public MemberVO join(MemberVO memberVO);

    public Optional<MemberVO> getKakaoMember(String memberEmail);

    // 로그인 시 프로필 이미지 업데이트
    public void updateProfileImgUrl(String memberEmail, String profileImgUrl);

    public Optional<MemberProfileDTO> getMemberProfileByMemberId(Long memberId);
}
