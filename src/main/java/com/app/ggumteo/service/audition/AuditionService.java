package com.app.ggumteo.service.audition;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.pagination.AuditionPagination;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuditionService {
    public void write(AuditionDTO auditionDTO, MultipartFile[] auditionFiles);

    AuditionDTO findAuditionById(Long id);

    List<AuditionDTO> findAllAuditions(PostType postType, String keyword, AuditionPagination pagination);

    void updateAudition(AuditionDTO auditionDTO, List<MultipartFile> newFiles, List<Long> deletedFileIds);
    void deleteAuditionById(Long id);

    int findTotal(PostType postType);

    int findTotalAuditionsSearch(PostType postType, String keyword);

    List<PostFileDTO> findAllPostFiles(Long postId);
}
