package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    //    파일 추가
    public void insert(FileVO fileVO);

    // 파일 삭제
    void deleteFileById(Long id);

    // 특정 게시물의 파일 조회
    List<FileVO> selectFileByPostId(@Param("postId") Long postId);
}
