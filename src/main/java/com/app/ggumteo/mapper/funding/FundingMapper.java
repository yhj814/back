package com.app.ggumteo.mapper.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FundingMapper {
//    내 게시물 조회
    public List<FundingDTO> selectByMemberId(Long memberId);
}
