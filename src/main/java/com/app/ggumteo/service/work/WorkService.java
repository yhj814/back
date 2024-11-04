package com.app.ggumteo.service.work;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkService {
    void write(WorkDTO workDTO, MultipartFile[] workFiles, MultipartFile thumbnailFile);

    WorkDTO findWorkById(Long id);

    void updateWork(WorkDTO workDTO, List<MultipartFile> newFiles, List<Long> deletedFileIds, MultipartFile newThumbnailFile);


    void deleteWorkById(Long id);

    void incrementReadCount(Long id);

    List<PostFileDTO> findFilesByPostId(Long postId);

    int findTotalWithSearchAndType(Search search);

    List<WorkDTO> findAllWithThumbnailAndSearchAndType(Search search, Pagination pagination);


    List<WorkDTO> getThreeWorksByGenre(String genreType, Long workId, String postType);

    List<WorkDTO> getThreeWorksByAuthor(Long memberProfileId, Long workId, String postType);
}
