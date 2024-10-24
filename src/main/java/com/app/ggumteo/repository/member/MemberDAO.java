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

    public Optional<MemberVO> findByKakaoEmail(String email) {
        return memberMapper.selectByMemberEmailForKakao(email);
    }
}
