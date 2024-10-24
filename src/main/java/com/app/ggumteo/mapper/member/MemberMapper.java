package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
//    카카오 회원 정보 조회
    public Optional<MemberVO> selectByMemberEmailForKakao(String email);
}
