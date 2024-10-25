package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.repository.audition.AuditionDAO;
import com.app.ggumteo.repository.post.PostDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuditionServiceImpl implements AuditionService {

    private final AuditionDAO auditionDAO;
    private final PostDAO postDAO;
    private final AuditionVO auditionVO;

    @Override
    public void write(AuditionDTO auditionDTO) {
        PostVO postVO = new PostVO();
        postVO.setId(auditionDTO.getId());
        postVO.setPostTitle(auditionDTO.getPostTitle());
        postVO.setPostContent(auditionDTO.getPostContent());
        postVO.setPostType(auditionDTO.getPostType());
        postVO.setMemberId(auditionDTO.getMemberId());

        postDAO.save(postVO);
        Long postId = postVO.getId();

        AuditionDTO toVO = new AuditionDTO();
        toVO.setId(postId);
        toVO.setAuditionField(auditionDTO.getAuditionField());
        toVO.setAuditionCareer(auditionDTO.getAuditionCareer());
        toVO.setExpectedAmount(auditionDTO.getExpectedAmount());
        toVO.setServiceStartDate(auditionDTO.getServiceStartDate());
        toVO.setAuditionDeadline(auditionDTO.getAuditionDeadline());
        toVO.setAuditionPersonnel(auditionDTO.getAuditionPersonnel());
        toVO.setAuditionLocation(auditionDTO.getAuditionLocation());
        toVO.setAuditionBackground(auditionDTO.getAuditionBackground());
        toVO.setAuditionCategory(auditionDTO.getAuditionCategory());
        toVO.setFileContent(auditionDTO.getFileContent());
        toVO.setAuditionStatus(auditionDTO.getAuditionStatus());

        auditionDAO.save(auditionVO);
    }


}
