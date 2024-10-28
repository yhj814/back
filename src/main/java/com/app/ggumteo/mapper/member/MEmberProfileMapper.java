package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MEmberProfileMapper {
    public void insert(MemberProfileVO memberProfileVO);
}
