package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.FileVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    //    파일 추가
    public void insert(FileVO fileVO);

}
