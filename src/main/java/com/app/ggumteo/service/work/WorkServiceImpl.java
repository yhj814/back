package com.app.ggumteo.service.work;


import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WorkServiceImpl implements WorkService {

    private final WorkDAO workDAO;
    private final PostDAO postDAO;

    @Override
    public void write(WorkDTO workDTO) {
        // Step 1: tbl_post에 데이터 삽입
        PostVO postVO = new PostVO();
        postVO.setPostTitle(workDTO.getPostTitle());
        postVO.setPostContent(workDTO.getPostContent());
        postVO.setPostType(workDTO.getPostType());
        postVO.setMemberId(workDTO.getMemberId());

        // Post를 저장하고 해당 ID를 가져옴
        postDAO.save(postVO);
        Long postId = postVO.getId();  // 삽입된 post의 ID를 가져옴

        // Step 2: tbl_work에 데이터 삽입 (postId를 id로 사용)
        WorkVO workVO = new WorkVO();
        workVO.setId(postId);  // tbl_work의 id를 tbl_post의 id와 동일하게 설정
        workVO.setWorkPrice(workDTO.getWorkPrice());
        workVO.setGenreType(workDTO.getGenreType());
        workVO.setReadCount(0);  // 기본값 설정
        workVO.setFileContent(workDTO.getFileContent());

        // Work 데이터를 저장
        workDAO.save(workVO);

        // workDTO에 저장된 postId를 설정
        workDTO.setId(postId);  // 파일 저장 시 사용할 수 있도록
    }
}

