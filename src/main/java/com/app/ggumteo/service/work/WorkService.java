package com.app.ggumteo.service.work;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;

import java.util.List;

public interface WorkService {

    // 작품 작성
    void write(WorkDTO workDTO);

    // 작품 ID로 조회
    WorkDTO findWorkById(Long id);

    // 작품 목록 조회 (썸네일 포함, 장르 필터 추가)
    List<WorkDTO> findAllWithThumbnail(String genreType);

    // 작품 정보 수정
    void updateWork(WorkDTO workDTO);

    // 작품 삭제
    void deleteWorkById(Long id);

    // 조회수 증가
    void incrementReadCount(Long id);

    // 총 작품 수 조회
    int findTotalWorks();

    // 다중 파일 조회 (작품 상세보기)
    List<PostFileDTO> findFilesByPostId(Long postId);
}
