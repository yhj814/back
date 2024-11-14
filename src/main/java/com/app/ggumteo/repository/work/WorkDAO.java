package com.app.ggumteo.repository.work;

import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.search.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkDAO {

    private final WorkMapper workMapper;
    private final PostDTO postDTO;

    // 게시글 작성
    public void save(WorkVO workVO) {
        workMapper.insert(workVO);
    }

    // 총 작품 수 조회
    public int findTotalWorks(String genreType) {
        return workMapper.selectTotalWithGenreType(genreType);  // Mapper에서 SQL 호출
    }

    // 작품 ID로 조회
    public WorkDTO findWorkById(Long id) {
        return workMapper.selectById(id);
    }

    public WorkDTO findWorkByIdForReplyAlarm(Long workId) {
        return workMapper.selectWorkById(workId);}

    // 조회수 증가
    public void incrementReadCount(Long id) {
        workMapper.increaseReadCount(id);
    }

    // 작품 정보 수정
    public void updateWork(WorkDTO workDTO) {
        workMapper.updateWork(workDTO);
    }

    public void updatePost(WorkDTO workDTO) {
        workMapper.updatePost(workDTO);
    }

    // 작품 삭제
    public void deleteWorkById(Long id) {
        workMapper.deleteById(id);
    }

    // 게시물 삭제
    public void deletePostById(Long id) {
        workMapper.deletePostById(id);
    }

    // 작품 목록 조회 (썸네일 포함, 검색 및 필터링 추가)
    public List<WorkDTO> findAllWithThumbnailAndSearchAndType(Search search, Pagination pagination) {
        return workMapper.selectAllWithThumbnailAndSearchAndType(search, pagination);
    }

    // 검색 조건이 포함된 총 작품 수 조회
    public int findTotalWithSearchAndType(Search search) {
        return workMapper.selectTotalWithSearchAndType(search);
    }




    // 다중 파일 조회 (작품 상세보기)
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return workMapper.selectFilesByPostId(postId);
    }

    // 같은 장르의 최신순 3개 작품 조회
    public List<WorkDTO> findThreeByGenre(String genreType, Long workId, String postType) {
        return workMapper.selectThreeByGenre(genreType, workId, postType);
    }

    // 작가의 다른 최신 작품 3개 조회
    public List<WorkDTO> findThreeByAuthor(Long memberProfileId, Long workId, String postType) {
        return workMapper.selectThreeByAuthor(memberProfileId, workId, postType);
    }

    // 썸네일 파일 ID 업데이트 메소드 추가
    public void updateThumbnailFileId(Long workId, Long thumbnailFileId) {
        workMapper.updateThumbnailFileId(workId, thumbnailFileId);
    }

//************ 마이페이지 **************

    // 내 작품 게시물 전체 조회
    public List<WorkDTO> findByMemberId(MyWorkAndFundingPagination myWorkAndFundingPagination, Long memberId, String postType) {
        return workMapper.selectByMemberId(myWorkAndFundingPagination, memberId, postType);
    }

    // 내 작품 게시물 전체 갯수
    public int getTotal(Long memberId, String postType){
        return workMapper.selectCount(memberId, postType);
    }

    // 작품 정보 조회
    public Optional<WorkDTO> findByIdAndPostType(Long id, String postType) {
        return workMapper.selectByIdAndPostType(id, postType);
    }




//    메인페이지
public WorkDTO selectMostReadTextWorkForMainPage() {
    return workMapper.selectMostReadTextWorkForMainPage();
}

    public WorkDTO selectMostReadVideoWorkForMainPage() {
        return workMapper.selectMostReadVideoWorkForMainPage();
    }
}
