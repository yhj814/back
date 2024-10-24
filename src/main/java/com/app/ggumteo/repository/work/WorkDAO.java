package com.app.ggumteo.repository.work;

import com.app.ggumteo.mapper.work.WorkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkDAO {
    private final WorkMapper workMapper;

}
