package com.app.ggumteo.repository.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.mapper.file.PostFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostFileDAO {

    @Autowired
    private PostFileMapper postFileMapper;

    // 게시물과 파일의 관계 저장
    public void insertPostFile(PostFileVO postFileVO) {
        postFileMapper.insertPostFile(postFileVO);
    }

    // 파일 정보 저장 (추가)
    public void insertFile(FileVO fileVO) {
        postFileMapper.insertFile(fileVO);
    }
}


