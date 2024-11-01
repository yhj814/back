package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.audition.AuditionDAO;
import com.app.ggumteo.repository.post.PostDAO;
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
public class AuditionServiceImpl implements AuditionService {

    private final AuditionDAO auditionDAO;
    private final PostDAO postDAO;
    private final AuditionVO auditionVO;
    private final PostFileService postFileService;  // 파일 저장 서비스 주입

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
                    postFileService.saveFile(file, auditionDTO.getId());
                }
            }
        }
    }

    @Override
    public AuditionDTO findAuditionById(Long id) {return auditionDAO.findAuditionById(id);}

    @Override
    public List<AuditionDTO> findAllAuditions(String keyword, AuditionPagination pagination) {
        pagination.progress();

        return auditionDAO.findAllAuditions(keyword, pagination);
    }

    @Override
    public int findTotal(){
        return auditionDAO.findTotalAuditions();
    }

    @Override
    public int findTotalAuditionsSearch(String keyword) {
        return auditionDAO.findTotalAuditionsSearch(keyword);
    }

    @Override
    public List<PostFileDTO> findAllPostFiles(Long postId) {return auditionDAO.findFilesByAuditionId(postId);}



}
