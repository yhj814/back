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

    public void insertPostFile(PostFileVO postFileVO) {
        postFileMapper.insertPostFile(postFileVO);
    }

    public void insertFile(FileVO fileVO) {
        postFileMapper.insertFile(fileVO);  // fileMapper.xml의 파일 삽입 쿼리를 사용
    }
}