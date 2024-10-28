package com.app.ggumteo.repository.file;

import com.app.ggumteo.domain.file.FileVO;

import com.app.ggumteo.domain.file.ProfileFileVO;

import com.app.ggumteo.mapper.file.ProfileFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileFileDAO {

    @Autowired
    private ProfileFileMapper profileFileMapper;

    // 게시물과 파일의 관계 저장
    public void insertProfileFile(ProfileFileVO profileFileVO) {
        profileFileMapper.insertProfileFile(profileFileVO);
    }

    // 파일 정보 저장 (추가)
    public void insertFile(FileVO fileVO) {
        profileFileMapper.insertFile(fileVO);
    }
}


