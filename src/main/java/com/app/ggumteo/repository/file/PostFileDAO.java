package com.app.ggumteo.repository.file;

import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.mapper.file.PostFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostFileDAO {
    private final PostFileMapper postFileMapper;

    // 게시물과 파일의 관계 저장
    public void insertPostFile(PostFileVO postFileVO) {
        postFileMapper.insertPostFile(postFileVO);
    }

    // 특정 게시물의 파일 관계 삭제
    public void deletePostFilesByPostId(Long postId) {
        postFileMapper.deletePostFileById(postId);
    }

    // 특정 파일 ID의 게시물-파일 관계 삭제
    public void deletePostFileByFileId(Long fileId) {
        postFileMapper.deletePostFileByFileId(fileId);
    }
}
