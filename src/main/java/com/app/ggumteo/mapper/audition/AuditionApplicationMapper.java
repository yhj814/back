package com.app.ggumteo.mapper.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditionApplicationMapper {

    void insert(AuditionApplicationVO auditionApplication);
}
