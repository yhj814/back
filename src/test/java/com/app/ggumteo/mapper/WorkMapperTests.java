package com.app.ggumteo.mapper;

import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.mapper.work.BuyWorkMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class WorkMapperTests {
    @Autowired
    private WorkMapper workMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BuyWorkMapper buyWorkMapper;

//    @Test
//    public void testSelectByMemberId() {
//        MemberVO memberVO = null;
//        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
//        memberVO = memberMapper.selectById(15L).get();
//        workAndFundingPagination.setTotal(workMapper.selectCount(memberVO.getId(), PostType.VIDEO.name()));
//        workAndFundingPagination.progress();
//        workMapper.selectByMemberId(
//                        workAndFundingPagination, memberVO.getId(), PostType.VIDEO.name()).stream()
//                .map(WorkDTO::toString).forEach(log::info);
//    }

//    @Test
//    public void testSelectByIdAndPostType() {
//        WorkDTO workDTO = new WorkDTO();
//        workDTO.setId(38L);
//        log.info("workDTO={}", workDTO);
//
//        Optional<WorkDTO> foundWork = workMapper.selectByIdAndPostType(workDTO.getId(), PostType.VIDEO.name());
//        foundWork.map(WorkDTO::toString).ifPresent(log::info);
//    }

//    @Test
//    public void testSelectByWorkPostId() {
//        WorkDTO workDTO = null;
//        SettingTablePagination settingTablePagination = new SettingTablePagination();
//        log.info("settingTablePagination={}", settingTablePagination);
//        workDTO = workMapper.selectByIdAndPostType(38L, PostType.VIDEO.name()).get();
//        log.info("workDTO={}", workDTO);
//        settingTablePagination.setTotal(buyWorkMapper.selectCount(workDTO.getId()));
//        log.info("settingTablePagination={}", settingTablePagination);
//        settingTablePagination.progress();
//        buyWorkMapper.selectByWorkPostId(
//                        settingTablePagination, workDTO.getId()).stream()
//                .map(BuyWorkDTO::toString).forEach(log::info);
//    }
}

