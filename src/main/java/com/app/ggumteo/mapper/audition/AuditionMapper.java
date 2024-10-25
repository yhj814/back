package com.app.ggumteo.mapper.audition;

import com.app.ggumteo.domain.audition.AuditionVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.ModelAttribute;

@Mapper
public interface AuditionMapper {

    void insert(AuditionVO auditionVO);
}
