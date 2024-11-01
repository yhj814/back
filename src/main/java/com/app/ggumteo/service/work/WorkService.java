package com.app.ggumteo.service.work;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkService {
    void write(WorkDTO workDTO, MultipartFile[] workFiles, MultipartFile thumbnailFile); // 파일 파라미터 추가

    WorkDTO findWorkById(Long id);

    List<WorkDTO> findAllWithThumbnailAndSearch(String genreType, String keyword, Pagination pagination);

    void updateWork(WorkDTO workDTO);

    void deleteWorkById(Long id);

    void incrementReadCount(Long id);

    int findTotalWorks(String genreType);

    List<PostFileDTO> findFilesByPostId(Long postId);

    int findTotalWithSearch(String genreType, String keyword);

    List<WorkDTO> getThreeWorksByGenre(String genreType, Long workId);

    List<WorkDTO> getThreeWorksByAuthor(Long memberProfileId, Long workId);
}
