package com.app.ggumteo.service.work;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.buy.BuyFundingProductDAO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WorkServiceImpl implements WorkService {

    private final WorkDAO workDAO;
    private final PostDAO postDAO;
    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;
    private final PostFileService postFileService;  // 파일 저장 서비스 주입


    @Override
    public void write(WorkDTO workDTO) {
        // 게시글 저장
        PostVO postVO = new PostVO();
        postVO.setPostTitle(workDTO.getPostTitle());
        postVO.setPostContent(workDTO.getPostContent());
        postVO.setPostType(workDTO.getPostType());
        postVO.setMemberProfileId(workDTO.getMemberProfileId());

        postDAO.save(postVO);
        Long postId = postVO.getId();
        if (postId == null) {
            throw new RuntimeException("게시글 ID 생성 실패");
        }

        // 작품 저장
        WorkVO workVO = new WorkVO();
        workVO.setId(postId); // 게시글 ID 사용
        workVO.setWorkPrice(workDTO.getWorkPrice());
        workVO.setGenreType(workDTO.getGenreType());
        workVO.setReadCount(0);
        workVO.setFileContent(workDTO.getFileContent());

        // 썸네일 파일 처리
        String thumbnailFileName = workDTO.getThumbnailFileName();
        if (thumbnailFileName != null) {
            FileVO thumbnailFileVO = new FileVO();
            thumbnailFileVO.setFileName(thumbnailFileName);
            thumbnailFileVO.setFilePath(getPath());
            File file = new File("C:/upload/" + getPath() + "/" + thumbnailFileName);
            thumbnailFileVO.setFileSize(String.valueOf(file.length()));

            try {
                thumbnailFileVO.setFileType(Files.probeContentType(file.toPath()));
            } catch (IOException e) {
                log.error("썸네일 파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                thumbnailFileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
            }

            // 파일 정보 데이터베이스에 저장
            fileDAO.save(thumbnailFileVO);
            workVO.setThumbnailFileId(thumbnailFileVO.getId());
        } else {
            workVO.setThumbnailFileId(null);
        }

        // 작품 저장
        workDAO.save(workVO);

        // 업로드된 파일 처리
        List<String> fileNames = workDTO.getFileNames();
        if (fileNames != null && !fileNames.isEmpty()) {
            for (String fileName : fileNames) {
                // 기존 메서드 사용
                FileVO fileVO = new FileVO();
                fileVO.setFileName(fileName);
                fileVO.setFilePath(getPath());
                File file = new File("C:/upload/" + getPath() + "/" + fileName);
                fileVO.setFileSize(String.valueOf(file.length()));

                try {
                    fileVO.setFileType(Files.probeContentType(file.toPath()));
                } catch (IOException e) {
                    log.error("파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                    fileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
                }

                // 파일 정보 데이터베이스에 저장
                fileDAO.save(fileVO);

                // 파일과 게시글의 연관 관계 설정
                PostFileVO postFileVO = new PostFileVO(postId, fileVO.getId());
                postFileDAO.insertPostFile(postFileVO);
            }
        }
    }



    @Override
    public WorkDTO findWorkById(Long id) {
        return workDAO.findWorkById(id);
    }

    @Override
    public List<WorkDTO> findAllWithThumbnailAndSearchAndType(Search search, Pagination pagination) {
        List<WorkDTO> works = workDAO.findAllWithThumbnailAndSearchAndType(search, pagination);

        for (WorkDTO work : works) {
            // 썸네일 파일 경로 설정 t_붙혀서 해놓음!!!!
            if (work.getThumbnailFileName() != null) {
                String thumbnailFileName = "t_" + work.getThumbnailFileName();
                String filePath = work.getThumbnailFilePath(); // 이 필드는 파일의 저장 경로를 나타냅니다.
                String fullPath = filePath + "/" + thumbnailFileName;
                work.setThumbnailFilePath(fullPath);
            }
        }

        return works;
    }

    @Override
    public void updateWork(WorkDTO workDTO, List<Long> deletedFileIds) {
        log.info("업데이트 요청 - 작품 ID: {}", workDTO.getId());
        log.info("삭제할 파일 IDs: {}", deletedFileIds);

        // 기존 데이터를 다시 조회하여 최신 정보를 가져옴
        WorkDTO currentWork = workDAO.findWorkById(workDTO.getId());
        Long currentThumbnailFileId = currentWork.getThumbnailFileId();

        // 썸네일 파일 교체 처리
        if (workDTO.getThumbnailFileName() != null && !workDTO.getThumbnailFileName().isEmpty()) {
            // 기존 썸네일 파일의 외래 키 참조 해제
            if (currentThumbnailFileId != null) {
                workDAO.updateThumbnailFileId(workDTO.getId(), null);  // 썸네일 파일 ID를 먼저 null로 설정
                fileDAO.deleteFile(currentThumbnailFileId);
                log.info("기존 썸네일 파일 삭제 완료: 썸네일 파일 ID: {}", currentThumbnailFileId);
            }

            // 새로운 썸네일 파일 정보 생성
            FileVO thumbnailFileVO = new FileVO();
            thumbnailFileVO.setFileName(workDTO.getThumbnailFileName());
            thumbnailFileVO.setFilePath(getPath());
            File file = new File("C:/upload/" + getPath() + "/" + workDTO.getThumbnailFileName());
            thumbnailFileVO.setFileSize(String.valueOf(file.length()));

            try {
                thumbnailFileVO.setFileType(Files.probeContentType(file.toPath()));
            } catch (IOException e) {
                log.error("썸네일 파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                thumbnailFileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
            }

            // 파일 정보 데이터베이스에 저장
            fileDAO.save(thumbnailFileVO);

            // 저장된 파일의 ID를 설정
            workDTO.setThumbnailFileId(thumbnailFileVO.getId());
            log.info("새로운 썸네일 파일 저장 완료: 썸네일 파일 ID: {}", thumbnailFileVO.getId());
        } else {
            // 새로운 썸네일 파일이 없을 경우, 기존 썸네일 파일 ID 유지
            workDTO.setThumbnailFileId(currentThumbnailFileId);
            log.info("새로운 썸네일 파일이 없습니다. 기존 썸네일 유지.");
        }

        // 기존 파일 삭제
        if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
            postFileService.deleteFilesByIds(deletedFileIds);
        }

        // 새 파일 추가 (썸네일 파일이 아닌 일반 파일들)
        if (workDTO.getFileNames() != null && !workDTO.getFileNames().isEmpty()) {
            for (String fileName : workDTO.getFileNames()) {
                if (fileName != null && !fileName.isEmpty()) {
                    FileVO newFileVO = new FileVO();
                    newFileVO.setFileName(fileName);
                    newFileVO.setFilePath(getPath());
                    File file = new File("C:/upload/" + getPath() + "/" + fileName);
                    newFileVO.setFileSize(String.valueOf(file.length()));

                    try {
                        newFileVO.setFileType(Files.probeContentType(file.toPath()));
                    } catch (IOException e) {
                        log.error("파일의 콘텐츠 타입을 결정하는 중 오류 발생", e);
                        newFileVO.setFileType("unknown");  // 기본값 설정 또는 예외 처리
                    }

                    // 파일 정보 데이터베이스에 저장
                    fileDAO.save(newFileVO);

                    // 파일과 게시물의 연관 관계 설정
                    PostFileVO postFileVO = new PostFileVO(workDTO.getId(), newFileVO.getId());
                    postFileDAO.insertPostFile(postFileVO);
                    log.info("새 파일 추가 완료: 파일 ID: {}", newFileVO.getId());
                }
            }
        }

        // 작품 정보 업데이트
        workDAO.updateWork(workDTO);
        if (workDTO.getPostTitle() != null && workDTO.getPostContent() != null) {
            workDAO.updatePost(workDTO);
        }
        log.info("작품 정보 업데이트 완료: 작품 ID {}", workDTO.getId());

        if (workDTO.getPostTitle() != null && workDTO.getPostContent() != null) {
            log.info("게시글 정보 업데이트 시도: 제목 - {}, 내용 - {}", workDTO.getPostTitle(), workDTO.getPostContent());
            workDAO.updatePost(workDTO);
            log.info("게시글 정보 업데이트 완료: 작품 ID {}", workDTO.getId());
        } else {
            log.warn("게시글 정보가 부족하여 업데이트를 건너뜁니다: 작품 ID {}", workDTO.getId());
        }
    }



    @Override
    public void deleteWorkById(Long id) {
        workDAO.deleteWorkById(id);
        postDAO.deleteById(id);
    }

    @Override
    public void incrementReadCount(Long id) {
        workDAO.incrementReadCount(id);
    }



    @Override
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        return workDAO.findFilesByPostId(postId);
    }

    @Override
    public int findTotalWithSearchAndType(Search search) {
        return workDAO.findTotalWithSearchAndType(search);
    }


    @Override
    public List<WorkDTO> getThreeWorksByGenre(String genreType, Long workId, String postType) {
        return workDAO.findThreeByGenre(genreType, workId, postType);
    }

    @Override
    public List<WorkDTO> getThreeWorksByAuthor(Long memberProfileId, Long workId, String postType) {
        return workDAO.findThreeByAuthor(memberProfileId, workId, postType);
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    @Override
    public WorkDTO getMostReadTextWorkForMainPage() {
        return workDAO.selectMostReadTextWorkForMainPage();
    }

    @Override
    public WorkDTO getMostReadVideoWorkForMainPage() {
        return workDAO.selectMostReadVideoWorkForMainPage();
    }


}
