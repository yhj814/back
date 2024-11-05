package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
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
        // tbl_post에 데이터 삽입
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

        // tbl_work에 데이터 삽입 (postId를 id로 사용)
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(postId);  // tbl_work의 id를 tbl_post의 id로 설정
        workDTO.setWorkPrice("1000");
        workDTO.setGenreType("1");

        // DTO를 VO로 변환
        WorkVO workVO = workDTO.toVO();

        // WorkMapper를 통해 데이터베이스에 저장
        workMapper.insert(workVO);
    }

    @Test
    public void testGetAllWorks() {
        // 작품 목록을 조회하고 출력
        workMapper.selectAllWithThumbnailAndSearchAndType(null, null).forEach(work -> log.info(work.toString()));
    }

    @Test
    public void testGetWorkById() {
        // 특정 ID로 작품을 조회하고 출력
        Long workId = 1L; // 테스트할 작품 ID
        WorkDTO work = workMapper.selectById(workId);
        if (work != null) {
            log.info(work.toString());
        } else {
            log.info("해당 ID로 작품을 찾을 수 없습니다: " + workId);
        }
    }

    @Test
    public void testUpdateWork() {
        // 수정할 작품의 ID 설정
        Long workId = 1L; // 테스트할 작품 ID

        // 수정할 작품 조회
        WorkDTO workDTO = workMapper.selectById(workId);
        if (workDTO == null) {
            log.info("해당 ID로 작품을 찾을 수 없습니다: " + workId);
            return;
        }

        // 작품 정보 수정
        workDTO.setPostTitle("수정된 제목");
        workDTO.setPostContent("수정된 글 내용");
        workDTO.setWorkPrice("2000");
        workDTO.setGenreType("2");

        // 수정된 내용을 데이터베이스에 반영
        workMapper.updateWork(workDTO);
        workMapper.updatePost(workDTO);

        // 수정된 작품 다시 조회 후 출력
        WorkDTO updatedWork = workMapper.selectById(workId);
        if (updatedWork != null) {
            log.info("수정된 작품 정보: " + updatedWork);
        } else {
            log.info("수정 후 해당 ID로 작품을 찾을 수 없습니다: " + workId);
        }
    }
}
