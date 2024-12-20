package com.app.ggumteo.mapper;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.buy.BuyWorkDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.buy.BuyWorkMapper;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.pagination.MySettingTablePagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class WorkMapperTests {
    @Autowired
    private WorkMapper workMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BuyWorkMapper buyWorkMapper;

    @Test
    public void testSelectByMemberId() {
        MemberVO memberVO = null;
        MyWorkAndFundingPagination myWorkAndFundingPagination = new MyWorkAndFundingPagination();
        memberVO = memberMapper.selectById(2L).get();
        myWorkAndFundingPagination.setTotal(workMapper.selectCount(memberVO.getId(), PostType.WORKVIDEO.name()));
        myWorkAndFundingPagination.progress();
        workMapper.selectByMemberId(
                        myWorkAndFundingPagination, memberVO.getId(), PostType.WORKVIDEO.name()).stream()
                .map(WorkDTO::toString).forEach(log::info);
    }

    @Test
    public void testSelectByIdAndPostType() {
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(5L);
        log.info("workDTO={}", workDTO);

        Optional<WorkDTO> foundWork = workMapper.selectByIdAndPostType(workDTO.getId(), PostType.WORKVIDEO.name());
        foundWork.map(WorkDTO::toString).ifPresent(log::info);
    }

    @Test
    public void testSelectByWorkPostId() {
        WorkDTO workDTO = null;
        MySettingTablePagination mySettingTablePagination = new MySettingTablePagination();
        log.info("mySettingTablePagination={}", mySettingTablePagination);
        workDTO = workMapper.selectByIdAndPostType(5L, PostType.WORKVIDEO.name()).get();
        log.info("workDTO={}", workDTO);
        mySettingTablePagination.setTotal(buyWorkMapper.selectCount(workDTO.getId()));
        log.info("mySettingTablePagination={}", mySettingTablePagination);
        mySettingTablePagination.progress();
        buyWorkMapper.selectByWorkPostId(
                        mySettingTablePagination, workDTO.getId()).stream()
                .map(BuyWorkDTO::toString).forEach(log::info);
    }
}

