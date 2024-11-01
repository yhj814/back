package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.pagination.Pagination;

import java.util.List;

public interface AuditionService {
    public void write(AuditionDTO auditionDTO);

    AuditionDTO findAuditionById(Long id);

    List<AuditionDTO> findAllAuditions(String keyword, AuditionPagination pagination);

    int findTotal();

    int findTotalAuditionsSearch(String keyword);

    List<PostFileDTO> findAllPostFiles(Long postId);
}
