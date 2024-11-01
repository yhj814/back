package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostFileMapper {

    void insertPostFile(PostFileVO postFileVO);  // 게시물과 파일 간의 관계 저장

    // 게시글-파일 관계 삭제
    void deletePostFileById(@Param("postId") Long postId);
}
