package com.app.ggumteo.service.work;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import com.app.ggumteo.search.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public WorkDTO findWorkById(Long id) {
        return workDAO.findWorkById(id);
    }

    @Override
    public List<WorkDTO> findAllWithThumbnailAndSearch(String genreType, String keyword, Pagination pagination) {
        pagination.progress2();  // 페이지네이션 계산

        // 검색 조건 설정
        Search search = new Search();
        search.setKeyword(keyword);

        // 검색된 작품 목록 조회
        return workDAO.findAllWithThumbnailAndSearch(search, genreType, pagination);
    }


    @Override
    public void updateWork(WorkDTO workDTO) {
        workDAO.updateWork(workDTO);
    }

    @Override
    public void deleteWorkById(Long id) {
        workDAO.deleteWorkById(id);
        postDAO.deleteById(id);  // 연관된 post 삭제
    }

    @Override
    public void incrementReadCount(Long id) {
        workDAO.incrementReadCount(id);
    }

    @Override
    public int findTotalWorks(String genreType) {
        return workDAO.findTotalWorks(genreType);  // DAO에서 장르 필터가 적용된 총 작품 수 조회
    }

    @Override
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return workDAO.findFilesByPostId(postId);
    }

    @Override
    public int findTotalWithSearch(String genreType, String keyword) {
        // 검색 조건을 Search 객체에 설정
        Search searchParams = new Search();
        searchParams.setGenreType(genreType);
        searchParams.setKeyword(keyword);

        // Search 객체를 사용하여 DAO 메서드 호출
        return workDAO.findTotalWithSearch(searchParams);
    }

}
