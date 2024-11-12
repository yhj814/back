package com.app.ggumteo.repository.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.mapper.audition.AuditionApplicationMapper;
import com.app.ggumteo.pagination.MyAuditionPagination;
import com.app.ggumteo.pagination.MySettingTablePagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditionApplicationDAO {
    private final AuditionApplicationMapper auditionApplicationMapper;

    public void save(AuditionApplicationDTO auditionApplicationDTO) {
        auditionApplicationMapper.insert(auditionApplicationDTO);
    }

    public int countApplicantsByAuditionId(Long auditionId) {
        return auditionApplicationMapper.countApplicantsByAuditionId(auditionId);
    }

//*********** 마이페이지 **************

    //    나의 모집 지원자 목록 조회
    public List<AuditionApplicationDTO> findByAuditionPostId(MySettingTablePagination mySettingTablePagination, Long auditionId) {
        return auditionApplicationMapper.selectByAuditionPostId(mySettingTablePagination, auditionId);
    }

    //    나의 모집 게시물 하나의 지원자 전체 갯수
    public int getTotal(Long auditionId){
        return auditionApplicationMapper.selectCount(auditionId);
    }

    //    확인 여부 체크
    public void updateConfirmStatus(AuditionApplicationVO auditionApplicationVO) {
        auditionApplicationMapper.updateConfirmStatus(auditionApplicationVO);
    }

    //   내가 신청한 모집 목록 조회
    public List<AuditionApplicationDTO> findMyAppliedAuditionList(MyAuditionPagination myAuditionPagination
            , Long memberId, String postType) {
        return auditionApplicationMapper.selectAppliedAuditionListByMember(myAuditionPagination, memberId, postType);
    }

    //  내가 신청한 모집 목록 전체 갯수
    public int getMyAuditionApplicationListTotal(Long memberId, String postType){
        return auditionApplicationMapper.selectCountAppliedAuditionListByMember(memberId, postType);
    }
}
