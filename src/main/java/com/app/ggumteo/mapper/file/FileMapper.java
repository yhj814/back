package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    // 파일 추가
    void insert(FileVO fileVO);

    // 파일 삭제 전 게시글-파일 관계를 먼저 삭제한 후 파일 삭제
    void deleteFileById(@Param("id") Long id);

    // 특정 게시물의 파일 조회
    List<FileVO> selectFileByPostId(@Param("postId") Long postId);
}
