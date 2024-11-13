package com.app.ggumteo.mapper.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuditionApplicationMapper {

    void insert(AuditionApplicationDTO auditionApplicationDTO);

    AuditionDTO findAuditionByauditionId(@Param("auditionId") Long auditionId);

    int countApplicantsByAuditionId(@Param("auditionId") Long auditionId);
}
