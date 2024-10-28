package com.app.ggumteo.mapper;


import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.mapper.post.PostMapper;
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

    @Autowired
    private PostMapper postMapper;

    @Test
    public void testInsert() {
        //tbl_post에 데이터 삽입
        PostVO postVO = new PostVO();
        postVO.setPostTitle("테스트제목1");
        postVO.setPostContent("테스트 글 내용1");
        postVO.setPostType("TEXT");  // 글 유형
        postVO.setMemberProfileId(1L);  // 회원 ID
        postMapper.insert(postVO);  // tbl_post에 데이터 삽입

        // Post ID가 제대로 설정되었는지 확인
        Long postId = postVO.getId();
        if (postId == null) {
            log.info("postId 없다!!!!!!!!!!!!!!!!!");
        }

        //tbl_work에 데이터 삽입 (postId를 id로 사용)
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(postId);  // tbl_work의 id를 tbl_post의 id로 설정
        workDTO.setWorkPrice("1000");
        workDTO.setGenreType("1");

        // DTO를 VO로 변환
        WorkVO workVO = workDTO.toVO();

        // WorkMapper를 통해 데이터베이스에 저장
        workMapper.insert(workVO);
    }
}
