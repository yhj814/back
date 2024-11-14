package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {
//    회원가입
    public void insert(MemberVO memberVO);

//    카카오 회원 정보 조회
    public Optional<MemberVO> selectByMemberEmailForKakao(String memberEmail);

    // 로그인 시 프로필 이미지 업데이트
    public void updateProfileImgUrl(@Param("memberEmail") String memberEmail, @Param("profileImgUrl") String profileImgUrl);

    public Optional<MemberProfileDTO> getMemberProfileByMemberId(Long memberId);


//   회원 정보 조회: 마이페이지에서 조회
    public Optional<MemberVO> selectById(Long id);

//   회원 탈퇴
    public void softDeleteMember(MemberVO memberVO);
}
