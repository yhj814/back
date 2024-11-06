package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditionApplicationFileMapper {
    void insertAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO);

    void insertFile(FileVO fileVO);
}
