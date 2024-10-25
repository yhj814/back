package com.app.ggumteo.mapper.post;

import com.app.ggumteo.domain.post.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostMapper {
    public void insert(PostVO postVO);

    // 마지막 삽입된 ID 가져오기
    @Select("select last_insert_id()")
    public Long selectLastInsertId();
}
