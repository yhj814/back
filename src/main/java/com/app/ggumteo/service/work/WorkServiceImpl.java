package com.app.ggumteo.service.work;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WorkServiceImpl implements WorkService {

    private final WorkDAO workDAO;
    private final PostDAO postDAO;
    private final PostFileService postFileService;  // 파일 저장 서비스 주입

    @Override
    public void write(WorkDTO workDTO, MultipartFile[] workFiles, MultipartFile thumbnailFile) {
        PostVO postVO = new PostVO();
        postVO.setPostTitle(workDTO.getPostTitle());
        postVO.setPostContent(workDTO.getPostContent());
        postVO.setPostType(workDTO.getPostType());
        postVO.setMemberProfileId(workDTO.getMemberProfileId());

        postDAO.save(postVO);
        Long postId = postVO.getId();

        WorkVO workVO = new WorkVO();
        workVO.setId(postId);
        workVO.setWorkPrice(workDTO.getWorkPrice());
        workVO.setGenreType(workDTO.getGenreType());
        workVO.setReadCount(0);
        workVO.setFileContent(workDTO.getFileContent());

        workDAO.save(workVO);
        workDTO.setId(postId);

        // 파일 저장 처리 (PostFileService에 위임)
        if (workFiles != null && workFiles.length > 0) {
            for (MultipartFile file : workFiles) {
                if (!file.isEmpty()) {
                    postFileService.saveFile(file, workDTO.getId());
                }
            }
        }
        if (!thumbnailFile.isEmpty()) {
            postFileService.saveFile(thumbnailFile, workDTO.getId());
        }
    }

    @Override
    public WorkDTO findWorkById(Long id) {
        return workDAO.findWorkById(id);
    }

    @Override
    public List<WorkDTO> findAllWithThumbnailAndSearch(String genreType, String keyword, Pagination pagination) {
        pagination.progress2();
        return workDAO.findAllWithThumbnailAndSearch(keyword, genreType, pagination);
    }

    @Override
    public void updateWork(WorkDTO workDTO) {
        workDTO.setPostId(workDTO.getId());
        workDAO.updateWork(workDTO);
        workDAO.updatePost(workDTO);
    }

    @Override
    public void deleteWorkById(Long id) {
        workDAO.deleteWorkById(id);
        postDAO.deleteById(id);
    }

    @Override
    public void incrementReadCount(Long id) {
        workDAO.incrementReadCount(id);
    }

    @Override
    public int findTotalWorks(String genreType) {
        return workDAO.findTotalWorks(genreType);
    }

    @Override
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return workDAO.findFilesByPostId(postId);
    }

    @Override
    public int findTotalWithSearch(String genreType, String keyword) {
        return workDAO.findTotalWithSearch(genreType, keyword);
    }

    @Override
    public List<WorkDTO> getThreeWorksByGenre(String genreType, Long workId) {
        return workDAO.findThreeByGenre(genreType, workId);
    }

    @Override
    public List<WorkDTO> getThreeWorksByAuthor(Long memberProfileId, Long workId) {
        return workDAO.findThreeByAuthor(memberProfileId, workId);
    }




}
