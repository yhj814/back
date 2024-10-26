package com.app.ggumteo.repository.work;

import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.mapper.work.WorkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkDAO {

    private final WorkMapper workMapper;

    // 게시글 작성
    public void save(WorkVO workVO) {
        workMapper.insert(workVO);
    }

    // 총 작품 수 조회
    public int findTotalWorks() {
        return workMapper.selectTotal();
    }

    // 작품 ID로 조회
    public WorkDTO findWorkById(Long id) {
        return workMapper.selectById(id);
    }

    // 조회수 증가
    public void incrementReadCount(Long id) {
        workMapper.increaseReadCount(id);
    }

    // 작품 정보 수정
    public void updateWork(WorkDTO workDTO) {
        workMapper.updateWork(workDTO);
    }

    // 작품 삭제
    public void deleteWorkById(Long id) {
        workMapper.deleteById(id);
    }

    // 게시물 삭제
    public void deletePostById(Long id) {
        workMapper.deletePostById(id);
    }

    // 작품 목록 조회 (썸네일 포함, 장르 필터 추가)
    public List<WorkDTO> findAllWithThumbnail(String genreType) {
        return workMapper.selectAllWithThumbnail(genreType);
    }

    // 검색 조건이 포함된 총 작품 수 조회
    public int findTotalWithSearch(WorkDTO workSearch) {
        return workMapper.selectTotalWithSearch(workSearch);
    }

    // 다중 파일 조회 (작품 상세보기)
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return workMapper.selectFilesByPostId(postId);
    }
}
