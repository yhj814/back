package com.app.ggumteo.repository.member;

import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.mapper.member.MemberProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberProfileDAO {
    private final MemberProfileMapper memberProfileMapper;

    public void save(MemberProfileVO memberProfileVO) {memberProfileMapper.insert(memberProfileVO);}
}
