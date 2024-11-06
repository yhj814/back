package com.app.ggumteo.mapper;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.funding.BuyFundingProductDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.funding.BuyFundingProductMapper;
import com.app.ggumteo.mapper.funding.FundingMapper;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j

public class WorkMapperTests {
    @Autowired
    private WorkMapper workMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void testSelectByMemberId() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = memberMapper.selectById(15L).get();
        workAndFundingPagination.setTotal(workMapper.selectCount(memberVO.getId(), PostType.VIDEO.name()));
        workAndFundingPagination.progress();
        workMapper.selectByMemberId(
                        workAndFundingPagination, memberVO.getId(), PostType.VIDEO.name()).stream()
                .map(WorkDTO::toString).forEach(log::info);
    }
}

