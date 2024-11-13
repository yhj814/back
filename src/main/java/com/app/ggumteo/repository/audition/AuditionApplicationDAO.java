package com.app.ggumteo.repository.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.mapper.audition.AuditionApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditionApplicationDAO {
    private final AuditionApplicationMapper auditionApplicationMapper;

    public void save(AuditionApplicationDTO auditionApplicationDTO) {
        auditionApplicationMapper.insert(auditionApplicationDTO);
    }

    public AuditionDTO findByAuditionId(Long auditionId) {
        return auditionApplicationMapper.findByAuditionId(auditionId);
    }

    public int countApplicantsByAuditionId(Long auditionId) {
        return auditionApplicationMapper.countApplicantsByAuditionId(auditionId);
    }
}
