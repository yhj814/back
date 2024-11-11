package com.app.ggumteo.mapper.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.domain.buy.BuyFundingProductDTO;
import com.app.ggumteo.domain.buy.BuyFundingProductVO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.repository.audition.AuditionApplicationDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditionApplicationMapper {

    void insert(AuditionApplicationDTO auditionApplicationDTO);

    int countApplicantsByAuditionId(@Param("auditionId") Long auditionId);

    //    나의 모집 지원자 목록 조회
    public List<AuditionApplicationDTO> selectByAuditionPostId(
            @Param("settingTablePagination") SettingTablePagination settingTablePagination , Long AuditionPostId);

    //    나의 모집  게시물 하나의 지원자 전체 갯수
    public int selectCount(Long AuditionPostId);

    //    확인 여부 체크
    public void updateConfirmStatus(AuditionApplicationVO auditionApplicationVO);

    //    내가 신청한 모집 목록 조회
    public List<AuditionApplicationDTO> selectAppliedAuditionListByMember
    (@Param("workAndFundingPagination") WorkAndFundingPagination workAndFundingPagination
            , @Param("memberId") Long memberId, @Param("postType") String postType);

    //    내가 신청한 모집 목록 전체 갯수
    public int selectCountAppliedAuditionListByMember
    (@Param("memberId") Long memberId, @Param("postType") String postType);

}
