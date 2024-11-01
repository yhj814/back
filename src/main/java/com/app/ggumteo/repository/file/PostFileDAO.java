package com.app.ggumteo.repository.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.mapper.file.PostFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostFileDAO {

    @Autowired
    private PostFileMapper postFileMapper;

    // 게시물과 파일의 관계 저장
    public void insertPostFile(PostFileVO postFileVO) {
        postFileMapper.insertPostFile(postFileVO);
    }
    // 게시물-파일 관계 삭제
    public void deletePostFilesByPostId(Long postId) {
        postFileMapper.deletePostFileById(postId);
    }

}


