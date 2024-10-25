package com.app.ggumteo.repository.audition;

import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.mapper.audition.AuditionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditionDAO {
    private final AuditionMapper auditionMapper;

//    모집글 작성
    public void save(AuditionVO auditionVO) {auditionMapper.insert(auditionVO);}
}
