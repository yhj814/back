package com.app.ggumteo.repository.work;

import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.mapper.work.WorkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkDAO {
    private final WorkMapper workMapper;

    //게시글 작성
    public void save(WorkVO workVO) {workMapper.insert(workVO);}

}
