package com.app.ggumteo.mapper;


import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.service.work.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j

public class WorkWriteMapperTests {
    @Autowired
   private WorkMapper workMapper;

    @Test
    public void testInsert() {
        WorkDTO workDTO = new WorkDTO();
        workDTO.setPostTitle("테스트제목1");
        workDTO.setPostContent("테스트 글 내용1");
        workDTO.setMemberId(1L);
        workDTO.setWorkPrice("1000");



    }
}
