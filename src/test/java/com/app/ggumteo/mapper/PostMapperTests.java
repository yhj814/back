package com.app.ggumteo.mapper;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.mapper.post.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PostMapperTests {
    @Autowired
    private PostMapper postMapper;

    @Test
    public void testInsert() {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostTitle("문의제목1");
        postDTO.setPostContent("문의합니다");
        postDTO.setPostType("INQUIRY");
        postMapper.insert(postDTO.toVO());
        log.info("{}","게시글 등록성공!");
    }
}
