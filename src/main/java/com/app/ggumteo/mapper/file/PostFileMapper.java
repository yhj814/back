package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostFileMapper {
    void insertPostFile(PostFileVO postFileVO);
    void insertFile(FileVO fileVO);  // tbl_file에 파일 데이터 삽입
}
