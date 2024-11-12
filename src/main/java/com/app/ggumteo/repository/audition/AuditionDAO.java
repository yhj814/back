package com.app.ggumteo.repository.audition;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.mapper.audition.AuditionMapper;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import com.app.ggumteo.search.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuditionDAO {
    private final AuditionMapper auditionMapper;

//    모집글 작성
    public void save(AuditionVO auditionVO) {auditionMapper.insert(auditionVO);}


//    작품 ID로 조회
    public AuditionDTO findAuditionById(Long id) {
        return auditionMapper.selectById(id);
    }

//    모집 수정
    public void updateAudition(AuditionDTO auditionDTO) {auditionMapper.updateAudition(auditionDTO);}
    public void updatePost(AuditionDTO auditionDTO) {auditionMapper.updatePost(auditionDTO);}

//    모집 삭제
    public void deleteAudition(Long id) {auditionMapper.deleteById(id);}

//    목록 조회
    public List<AuditionDTO> findAllAuditions(PostType postType, Search search, AuditionPagination pagination) {
        return auditionMapper.selectAll(postType, search, pagination);}

//    검색조건이 포함된 총 모집 수
    public int findTotalAuditionsSearch(PostType postType, Search search) {
        return auditionMapper.selectTotalWithSearch(postType, search);
    }

//    다중 파일 조회
    public List<PostFileDTO> findFilesByAuditionId(Long postId) {return auditionMapper.selectFilesByPostId(postId);}


//************ 마이페이지 **************

    // 내 작품 게시물 전체 조회
    public List<AuditionDTO> findByMemberId(MyAuditionPagination myAuditionPagination, Long memberId, String postType) {
        return auditionMapper.selectByMemberId(myAuditionPagination, memberId, postType);
    }

    // 내 작품 게시물 전체 갯수
    public int getTotal(Long memberId, String postType){
        return auditionMapper.selectCount(memberId, postType);
    }

    // 작품 정보 조회
    public Optional<AuditionDTO> findByIdAndPostType(Long id, String postType) {
        return auditionMapper.selectByIdAndPostType(id, postType);
    };
}
