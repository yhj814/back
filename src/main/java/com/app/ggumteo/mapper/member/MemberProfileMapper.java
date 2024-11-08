package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberProfileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberProfileMapper {
    // 새로운 회원 프로필 삽입 및 자동 생성된 ID 반환
    void insert(MemberProfileVO memberProfileVO);

    // 마이페이지 - 내 정보 조회
    public Optional<MemberProfileVO> selectByMemberId(Long memberId);

    // 마이페이지 - 내 정보 수정
    public void updateByMemberId(MemberProfileVO memberProfileVO);
}
