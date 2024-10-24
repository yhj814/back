package com.app.ggumteo.repository.post;


import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.post.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostDAO {
    private final PostMapper postMapper;

    public void save(PostVO postVO) {postMapper.insert(postVO);}
}
