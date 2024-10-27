package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
//    회원가입
    public void insert(MemberVO memberVO);

//    카카오 회원 정보 조회
    public Optional<MemberVO> selectByMemberEmailForKakao(String memberEmail);

//   회원 정보 조회: 마이페이지 목록 조회할 때 member id 조회가 필요하여 작성함.
    public Optional<MemberVO> selectById(Long id);

}
