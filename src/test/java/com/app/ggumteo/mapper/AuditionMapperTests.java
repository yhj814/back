package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.audition.AuditionMapper;
import com.app.ggumteo.mapper.post.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AuditionMapperTests {

    @Autowired
    private AuditionMapper auditionMapper;

    @Autowired
    private PostMapper postMapper;

    @Test
    public void testInsert() {
//        post테이블에 데이터 삽입
        PostVO postVO = new PostVO();
        postVO.setPostTitle("모집TEST");
        postVO.setPostContent("모집내용 TEST입니다. 모집내용 TEST입니다. 모집내용 TEST입니다. 모집내용 TEST입니다. ");
        postVO.setPostType("VIDEO");
        postVO.setMemberId(1L);
        postMapper.insert(postVO);

        Long postId = postVO.getId();
        if(postId == null) {
            log.info("postId is null");
        }

//        audition테이블에 데이터 삽입(postId사용)
        AuditionDTO auditionDTO = new AuditionDTO();
        auditionDTO.setId(postId);
        auditionDTO.setAuditionField(1);
        auditionDTO.setAuditionCareer("경력무관");
        auditionDTO.setExpectedAmount("100000/일");
        auditionDTO.setServiceStartDate("2024.11.04");
        auditionDTO.setAuditionDeadline("2024.11.24");
        auditionDTO.setAuditionPersonnel("4");
        auditionDTO.setAuditionLocation("서울특별시 강남구 역삼역 3번출구");
        auditionDTO.setAuditionBackground("TEST입니다.");
        auditionDTO.setAuditionCategory("단편영화 배우");
        auditionDTO.setAuditionStatus("모집중");

        AuditionVO auditionVO = auditionDTO.toVO();
        auditionMapper.insert(auditionVO);
    }
}
