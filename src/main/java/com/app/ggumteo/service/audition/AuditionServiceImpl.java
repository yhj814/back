package com.app.ggumteo.service.audition;

import com.app.ggumteo.aspect.LogAspect;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.audition.AuditionDAO;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AuditionServiceImpl implements AuditionService {

    private final AuditionDAO auditionDAO;
    private final PostDAO postDAO;
    private final AuditionVO auditionVO;
    private final PostFileService postFileService;  // 파일 저장 서비스 주입
    private final LogAspect logAspect;

    @Override
    public void write(AuditionDTO auditionDTO, MultipartFile[] auditionFiles) {
        PostVO postVO = new PostVO();
        postVO.setPostTitle(auditionDTO.getPostTitle());
        postVO.setPostContent(auditionDTO.getPostContent());
        postVO.setPostType(auditionDTO.getPostType());
        postVO.setMemberProfileId(auditionDTO.getMemberProfileId());

        postDAO.save(postVO);
        Long postId = postVO.getId();

        // AuditionDTO의 데이터를 AuditionVO로 복사
        auditionVO.setId(postId);
        auditionVO.setAuditionField(auditionDTO.getAuditionField());
        auditionVO.setAuditionCareer(auditionDTO.getAuditionCareer());
        auditionVO.setExpectedAmount(auditionDTO.getExpectedAmount());
        auditionVO.setServiceStartDate(auditionDTO.getServiceStartDate());
        auditionVO.setAuditionDeadLine(auditionDTO.getAuditionDeadLine());
        auditionVO.setAuditionPersonnel(auditionDTO.getAuditionPersonnel());
        auditionVO.setAuditionLocation(auditionDTO.getAuditionLocation());
        auditionVO.setAuditionBackground(auditionDTO.getAuditionBackground());
        auditionVO.setAuditionCategory(auditionDTO.getAuditionCategory());
        auditionVO.setFileContent(auditionDTO.getFileContent());
        auditionVO.setAuditionStatus(auditionDTO.getAuditionStatus());

        // 완전히 설정된 auditionVO를 저장
        auditionDAO.save(auditionVO);
        auditionDTO.setId(postId);

        // 파일 저장 로직 호출 (auditionFile 저장)
        if (auditionFiles != null && auditionFiles.length > 0) {
            for (MultipartFile file : auditionFiles) {
                if (!file.isEmpty()) {
//                    postFileService.saveFile(file, auditionDTO.getId());
                }
            }
        }
    }

    @Override
    public AuditionDTO findAuditionById(Long id) {return auditionDAO.findAuditionById(id);}

    @Override
    public List<AuditionDTO> findAllAuditions(PostType postType, Search search, AuditionPagination pagination) {
        pagination.progress();

        return auditionDAO.findAllAuditions(postType, search, pagination);
    }

    @Override
    public void updateAudition(AuditionDTO auditionDTO, List<MultipartFile> newFiles, List<Long> deletedFileIds) {
        log.info("업데이트 요청 - 작품 ID: {}", auditionDTO.getId());
        log.info("삭제할 파일 IDs: {}", deletedFileIds);
        log.info("새로 추가할 파일 수: {}", (newFiles != null ? newFiles.size() : 0));
        auditionDTO.setPostId(auditionDTO.getId());

        AuditionDTO currentAudition = auditionDAO.findAuditionById(auditionDTO.getId());

        if (currentAudition != null) {
            // currentAudition에서 id 값을 auditionDTO로 설정
            auditionDTO.setId(currentAudition.getId());

            // 기존 파일 삭제
            if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
                postFileService.deleteFilesByIds(deletedFileIds);
            }

            // 새 파일 추가
            if (newFiles != null && !newFiles.isEmpty()) {
                for (MultipartFile file : newFiles) {
                    if (!file.isEmpty()) {
//                        postFileService.saveFile(file, auditionDTO.getId());
                    }
                }
            }

            // 모집 정보 업데이트
            auditionDAO.updateAudition(auditionDTO);
            log.info("작품 정보 업데이트 완료: 작품 ID: {}", auditionDTO.getId());
            auditionDAO.updatePost(auditionDTO);
        } else {
            log.warn("업데이트할 작품을 찾을 수 없습니다. 작품 ID: {}", auditionDTO.getId());
        }

    }


    @Override
    public void deleteAuditionById(Long id) {
        auditionDAO.deleteAudition(id);
        postDAO.deleteById(id);
    }

    @Override
    public int findTotalAuditionsSearch(PostType postType, Search search) {
        return auditionDAO.findTotalAuditionsSearch(postType, search);
    }

    @Override
    public List<PostFileDTO> findAllPostFiles(Long postId) {return auditionDAO.findFilesByAuditionId(postId);}



}
