package com.app.ggumteo.repository.audition;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.audition.AuditionMapper;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.pagination.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditionDAO {
    private final AuditionMapper auditionMapper;

//    모집글 작성
    public void save(AuditionVO auditionVO) {auditionMapper.insert(auditionVO);}

//    총 모집 수 조회
    public int findTotalAuditions() {return auditionMapper.selectTotal();}

//    작품 ID로 조회
    public AuditionDTO findAuditionById(Long id) {
        return auditionMapper.selectById(id);
    }

//    모집 수정
    public void updateAudition(AuditionDTO auditionDTO) {auditionMapper.updateAudition(auditionDTO);}

//    모집 삭제
    public void deleteAudition(Long id) {auditionMapper.deleteById(id);}

//    목록 조회
    public List<AuditionDTO> findAllAuditions(String keyword, AuditionPagination pagination) {
        return auditionMapper.selectAll(keyword, pagination);}

//    검색조건이 포함된 총 모집 수
    public int findTotalAuditionsSearch(String keyword) {
        return auditionMapper.selectTotalWithSearch(keyword);
    }

//    다중 파일 조회
    public List<PostFileDTO> findFilesByAuditionId(Long postId) {return auditionMapper.selectFilesByPostId(postId);}

}
