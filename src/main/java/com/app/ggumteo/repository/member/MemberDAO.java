package com.app.ggumteo.repository.member;

import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.audition.AuditionMapper;
import com.app.ggumteo.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDAO {
    private final MemberMapper memberMapper;

    public void save(MemberVO memberVO) {memberMapper.insert(memberVO);}

    public Optional<MemberVO> findByKakaoEmail(String memberEmail) {
        return memberMapper.selectByMemberEmailForKakao(memberEmail);
    }

    // 로그인 시 프로필 이미지 업데이트
    public void updateProfileImgUrl(String memberEmail, String profileImgUrl) {
        memberMapper.updateProfileImgUrl(memberEmail, profileImgUrl);
    }

    public Optional<MemberProfileDTO> findByMemberId(Long memberId) {
        return memberMapper.getMemberProfileByMemberId(memberId);
    }



    //   회원 정보 조회: 마이페이지에서 조회
    public Optional<MemberVO> findById(Long id) {
        return memberMapper.selectById(id);
    };

    //   회원 탈퇴
    public void softDeleteMember(MemberVO memberVO) {
        memberMapper.softDeleteMember(memberVO);
    };
}
