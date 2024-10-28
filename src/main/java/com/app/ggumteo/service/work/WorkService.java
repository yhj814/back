package com.app.ggumteo.service.work;

import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;

import java.util.List;

public interface WorkService {

    // 작품 작성
    void write(WorkDTO workDTO);

    // 작품 ID로 조회
    WorkDTO findWorkById(Long id);

    // 작품 목록 조회 (썸네일 포함, 장르 필터 및 검색 추가)
    List<WorkDTO> findAllWithThumbnailAndSearch(String genreType, String keyword, Pagination pagination);

    // 작품 정보 수정
    void updateWork(WorkDTO workDTO);

    // 작품 삭제
    void deleteWorkById(Long id);

    // 조회수 증가
    void incrementReadCount(Long id);

    // 총 작품 수 조회 (장르 필터 적용)
    int findTotalWorks(String genreType);

    // 검색 조건이 포함된 총 작품 수 조회
    int findTotalWithSearch(String genreType, String keyword);

    // 상세보기에서 다중 파일 조회
    List<PostFileDTO> findFilesByPostId(Long postId);
}
