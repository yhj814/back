package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.PostFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostFileMapper {

    // 게시물과 파일 간의 관계 저장
    void insertPostFile(PostFileVO postFileVO);

    // 특정 postId로 게시글-파일 관계 삭제
    void deletePostFileById(@Param("postId") Long postId);

    // 특정 fileId로 게시글-파일 관계 삭제
    void deletePostFileByFileId(@Param("fileId") Long fileId);
}
