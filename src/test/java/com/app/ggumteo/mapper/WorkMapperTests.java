package com.app.ggumteo.mapper;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.buy.BuyWorkDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.buy.BuyWorkMapper;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
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
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = memberMapper.selectById(2L).get();
        workAndFundingPagination.setTotal(workMapper.selectCount(memberVO.getId(), PostType.WORKVIDEO.name()));
        workAndFundingPagination.progress();
        workMapper.selectByMemberId(
                        workAndFundingPagination, memberVO.getId(), PostType.WORKVIDEO.name()).stream()
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
        SettingTablePagination settingTablePagination = new SettingTablePagination();
        log.info("settingTablePagination={}", settingTablePagination);
        workDTO = workMapper.selectByIdAndPostType(5L, PostType.WORKVIDEO.name()).get();
        log.info("workDTO={}", workDTO);
        settingTablePagination.setTotal(buyWorkMapper.selectCount(workDTO.getId()));
        log.info("settingTablePagination={}", settingTablePagination);
        settingTablePagination.progress();
        buyWorkMapper.selectByWorkPostId(
                        settingTablePagination, workDTO.getId()).stream()
                .map(BuyWorkDTO::toString).forEach(log::info);
    }
}

