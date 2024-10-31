package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberProfileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberProfileMapper {
    // 새로운 회원 프로필 삽입 및 자동 생성된 ID 반환
    void insert(MemberProfileVO memberProfileVO);
}
