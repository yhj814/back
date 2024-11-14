package com.app.ggumteo.mapper.post;

import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.post.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    public void insert(PostVO postVO);

//    문의하기
    public void insertInquiry(PostVO postVO);
//    작성게시글 마지막 ID 가져오기
    public Long getLastInsertId();

   public void deleteById(Long id);

   public PostDTO selectPostById(@Param("id") Long id);
}
