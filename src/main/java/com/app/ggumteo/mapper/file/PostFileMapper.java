package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostFileMapper {

    void insertPostFile(PostFileVO postFileVO);  // 게시물과 파일 간의 관계 저장

    void insertFile(FileVO fileVO);  // 파일 정보 저장 (새로 추가된 메서드)
}
