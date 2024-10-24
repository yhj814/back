package com.app.ggumteo.mapper.work;

import com.app.ggumteo.domain.work.WorkVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkMapper {

    public void insert(WorkVO workVO);
}
