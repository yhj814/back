package com.app.ggumteo.repository.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.mapper.audition.AuditionApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditionApplicationDAO {
    private final AuditionApplicationMapper auditionApplicationMapper;

    public void save(AuditionApplicationVO auditionApplication) {
        auditionApplicationMapper.insert(auditionApplication);
    }
}
