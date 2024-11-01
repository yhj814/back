package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.pagination.AuditionPagination;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuditionService {
    public void write(AuditionDTO auditionDTO, MultipartFile[] auditionFiles);

    AuditionDTO findAuditionById(Long id);

    List<AuditionDTO> findAllAuditions(String keyword, AuditionPagination pagination);

    int findTotal();

    int findTotalAuditionsSearch(String keyword);

    List<PostFileDTO> findAllPostFiles(Long postId);
}
