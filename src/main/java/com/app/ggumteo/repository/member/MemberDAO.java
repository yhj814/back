package com.app.ggumteo.repository.member;

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
    private final AuditionMapper auditionMapper;

    public void save(MemberVO memberVO) {memberMapper.insert(memberVO);}

    public Optional<MemberVO> findByKakaoEmail(String memberEmail) {
        return memberMapper.selectByMemberEmailForKakao(memberEmail);
    }

    // 로그인 시 프로필 이미지 업데이트
    public void updateProfileImgUrl(String memberEmail, String profileImgUrl) {
        memberMapper.updateProfileImgUrl(memberEmail, profileImgUrl);
    }






    //   회원 정보 조회: 마이페이지 목록 조회할 때 member id 조회가 필요하여 작성함.
    public Optional<MemberVO> findById(Long id) {
        return memberMapper.selectById(id);
    };
}
