package com.app.ggumteo.mapper.post;

import com.app.ggumteo.domain.post.PostVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    public void insert(PostVO postVO);
}
