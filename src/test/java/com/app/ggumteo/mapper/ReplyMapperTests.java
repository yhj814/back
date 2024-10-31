package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.mapper.reply.ReplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class ReplyMapperTests {

    @Autowired
    private ReplyMapper replyMapper;

    @Test
    public void testInsertReply() {
        // 새로운 댓글 객체 생성
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyContent("테스트 댓글 내용");
        replyDTO.setWorkId(12L); // 테스트할 post_id 값
        replyDTO.setStar(5); // 예시 별점
        replyDTO.setMemberProfileId(1L); // 테스트할 member_profile_id 값

        // 댓글 삽입 메서드 호출
        replyMapper.insertReply(replyDTO);

        // 삽입된 댓글의 ID가 할당되었는지 확인
        assertNotNull(replyDTO.getId(), "댓글 삽입에 실패했습니다.");

        log.info("댓글이 삽입되었습니다. ID: " + replyDTO.getId());
    }


}
