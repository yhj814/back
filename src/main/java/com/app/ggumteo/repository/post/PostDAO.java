package com.app.ggumteo.repository.post;


import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.post.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostDAO {
    private final PostMapper postMapper;

    public void save(PostVO postVO) {postMapper.insert(postVO);}

    // 문의사항 작성
    public void saveInquiry(PostVO post) {
        postMapper.insertInquiry(post);
    }

    // 문의사항 작성글 마지막 ID 가져오기
    public Long getLastInsertId() {
        return postMapper.getLastInsertId();
    }

    public void deleteById(Long id) {
        postMapper.deleteById(id);
    }

    public PostDTO findPostById(Long postId) {
        return postMapper.selectPostById(postId);
    }
}
