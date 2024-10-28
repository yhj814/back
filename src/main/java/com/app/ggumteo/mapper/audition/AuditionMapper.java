package com.app.ggumteo.mapper.audition;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.work.WorkDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Mapper
public interface AuditionMapper {

    // 작품 삽입
    void insert(AuditionVO auditionVO);

    // 총 작품 수 조회
    int selectTotal();

    // 작품 ID로 작품 조회
    AuditionDTO selectById(Long id);

    // 작품 정보 수정 (tbl_work 및 tbl_post 업데이트)
    void updateAudition(AuditionDTO auditionDTO);

    // 모집 삭제
    void deleteById(Long id);

    // post 삭제
    void deletePostById(Long id);

    // 전체목록 조회
    List<AuditionDTO> selectAll();

    // 검색 조건이 포함된 총 작품 수 조회
    int selectTotalWithSearch(@Param("auditionSearch") AuditionDTO auditionDTO);

}
