package com.app.ggumteo.repository.member;

import com.app.ggumteo.domain.member.MemberVO;
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
}
