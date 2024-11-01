package com.app.ggumteo.repository.file;


import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.mapper.file.FileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileDAO {
    private final FileMapper fileMapper;
    //    파일 추가
    public void save(FileVO fileVO){
        fileMapper.insert(fileVO);
    }

    // 파일 삭제
    public void deleteFile(Long id) {
        fileMapper.deleteFileById(id);
    }

    // 특정 게시물의 모든 파일 조회
    public List<FileVO> findFileByPostId(Long postId) {
        return fileMapper.selectFileByPostId(postId); // List<FileVO>를 반환
    }

}
