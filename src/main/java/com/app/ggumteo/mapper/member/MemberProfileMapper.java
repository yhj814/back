package com.app.ggumteo.mapper.member;

import com.app.ggumteo.domain.member.MemberProfileVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberProfileMapper {
    public void insert(MemberProfileVO memberProfileVO);
}
