package com.app.ggumteo.service.audition;

import com.app.ggumteo.aspect.LogAspect;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.audition.AuditionDAO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AuditionServiceImpl implements AuditionService {

    private final AuditionDAO auditionDAO;
    private final PostDAO postDAO;
    private final PostFileService postFileService;  // 파일 저장 서비스 주입
    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;

    @Override
    public void write(AuditionDTO auditionDTO) {
        PostVO postVO = new PostVO();
        postVO.setPostTitle(auditionDTO.getPostTitle());
        postVO.setPostContent(auditionDTO.getPostContent());
        postVO.setPostType(auditionDTO.getPostType());
        postVO.setMemberProfileId(auditionDTO.getMemberProfileId());

        postDAO.save(postVO);
        Long postId = postVO.getId();
        if (postId == null) {
            throw new RuntimeException("게시글 ID 생성 실패");
        }

        // AuditionDTO의 데이터를 AuditionVO로 복사
        AuditionVO auditionVO = new AuditionVO();
        auditionVO.setId(postId);
        auditionVO.setAuditionField(auditionDTO.getAuditionField());
        auditionVO.setAuditionCareer(auditionDTO.getAuditionCareer());
        auditionVO.setExpectedAmount(auditionDTO.getExpectedAmount());
        auditionVO.setServiceStartDate(auditionDTO.getServiceStartDate());
        auditionVO.setAuditionDeadLine(auditionDTO.getAuditionDeadLine());
        auditionVO.setAuditionPersonnel(auditionDTO.getAuditionPersonnel());
        auditionVO.setAuditionLocation(auditionDTO.getAuditionLocation());
        auditionVO.setAuditionBackground(auditionDTO.getAuditionBackground());
        auditionVO.setAuditionCategory(auditionDTO.getAuditionCategory());
        auditionVO.setFileContent(auditionDTO.getFileContent());
        auditionVO.setAuditionStatus(auditionDTO.getAuditionStatus());

        // 완전히 설정된 auditionVO를 저장
        auditionDAO.save(auditionVO);
        auditionDTO.setId(postId);

        // 업로드된 파일 처리
        List<String> fileNames = auditionDTO.getFileNames();
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
    public AuditionDTO findAuditionById(Long id) {return auditionDAO.findAuditionById(id);}

    @Override
    public List<AuditionDTO> findAllAuditions(PostType postType, Search search, AuditionPagination pagination) {
        pagination.progress();

        return auditionDAO.findAllAuditions(postType, search, pagination);
    }

    @Override
    public void updateAudition(AuditionDTO auditionDTO, List<Long> deletedFileIds) {
        log.info("업데이트 요청 - 작품 ID: {}", auditionDTO.getId());
        log.info("삭제할 파일 IDs: {}", deletedFileIds);

        auditionDTO.setPostId(auditionDTO.getId());

        AuditionDTO currentAudition = auditionDAO.findAuditionById(auditionDTO.getId());

        if (currentAudition != null) {
            // currentAudition에서 id 값을 auditionDTO로 설정
            auditionDTO.setId(currentAudition.getId());

            // 기존 파일 삭제
            if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
                postFileService.deleteFilesByIds(deletedFileIds);
            }

            // 새 파일 추가
            if (auditionDTO.getFileNames() != null && !auditionDTO.getFileNames().isEmpty()) {
                for (String fileName : auditionDTO.getFileNames()) {
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
                        PostFileVO postFileVO = new PostFileVO(auditionDTO.getId(), newFileVO.getId());
                        postFileDAO.insertPostFile(postFileVO);
                        log.info("새 파일 추가 완료: 파일 ID: {}", newFileVO.getId());
                    }
                }
            }

            // 모집 정보 업데이트
            auditionDAO.updateAudition(auditionDTO);
            log.info("작품 정보 업데이트 완료: 작품 ID: {}", auditionDTO.getId());
            auditionDAO.updatePost(auditionDTO);
        } else {
            log.warn("업데이트할 작품을 찾을 수 없습니다. 작품 ID: {}", auditionDTO.getId());
        }

    }


    @Override
    public void deleteAuditionById(Long id) {
        auditionDAO.deleteAudition(id);
        postDAO.deleteById(id);
    }

    @Override
    public int findTotalAuditionsSearch(PostType postType, Search search) {
        return auditionDAO.findTotalAuditionsSearch(postType, search);
    }

    @Override
    public List<PostFileDTO> findAllPostFiles(Long postId) {return auditionDAO.findFilesByAuditionId(postId);}


private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

}
