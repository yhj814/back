package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PostFileServiceImpl implements PostFileService {

    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;
    private final WorkDTO workDTO;

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        postFileDAO.insertPostFile(postFileVO);
    }

    @Override
    public FileVO saveFile(MultipartFile file) {
        String rootPath = "C:/upload/" + getPath() + "/";
        File directory = new File(rootPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        File saveLocation = new File(rootPath + uniqueFileName);
        try {
            file.transferTo(saveLocation);

            // 이미지 파일인 경우 썸네일 생성
            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                String thumbnailFileName = "t_" + uniqueFileName;
                File thumbnailFile = new File(rootPath + thumbnailFileName);

                // 썸네일 이미지 생성
                Thumbnails.of(saveLocation)
                        .size(300, 300)
                        .toFile(thumbnailFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 파일 정보 객체 생성 (데이터베이스에 저장하지 않음)
        FileVO fileVO = new FileVO();
        fileVO.setFileName(uniqueFileName);
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath(getPath());

        // 데이터베이스에 저장하지 않고 파일 정보만 반환
        return fileVO;
    }



    @Override
    public byte[] getFileData(String fileName) {
        String rootPath = "C:/upload/";
        File file = new File(rootPath + fileName);
        try {
            if (file.exists()) {
                return Files.readAllBytes(file.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
        return null;
    }

    @Override
    public List<PostFileDTO> uploadFile(List<MultipartFile> files) throws IOException {
        List<PostFileDTO> fileDTOs = new ArrayList<>();

        String rootPath = "C:/upload/" + getPath();
        File directory = new File(rootPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String uniqueFileName = uuid.toString() + "_" + file.getOriginalFilename();
            String relativePath = getPath() + "/" + uniqueFileName;

            // 파일 정보 객체 생성
            PostFileDTO postFileDTO = new PostFileDTO();
            postFileDTO.setFileName(uniqueFileName);
            postFileDTO.setFilePath(getPath());
            postFileDTO.setFileType(file.getContentType());
            postFileDTO.setFileSize(String.valueOf(file.getSize()));

            // 파일 시스템에 저장
            File saveLocation = new File(rootPath, uniqueFileName);
            file.transferTo(saveLocation);

            // 썸네일 생성 (필요한 경우)
            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                try (FileOutputStream thumbnail = new FileOutputStream(new File(rootPath, "t_" + uniqueFileName))) {
                    Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 300, 300);
                }
            }

            // 데이터베이스에는 저장하지 않음

            fileDTOs.add(postFileDTO);
        }
        log.info("업로드된 파일 정보 목록: {}", fileDTOs);
        return fileDTOs;
    }


    @Override
    public void deleteFilesByIds(List<Long> fileIds) {
        fileIds.forEach(fileId -> {
            postFileDAO.deletePostFileByFileId(fileId); // 관계 삭제
            fileDAO.deleteFile(fileId); // 파일 삭제
        });
    }

    @Override
    public List<PostFileDTO> findFilesByPostId(Long postId) {
        List<FileVO> fileVOList = fileDAO.findFileByPostId(postId);
        List<PostFileDTO> postFileDTOList = new ArrayList<>();

        for (FileVO fileVO : fileVOList) {
            PostFileDTO postFileDTO = new PostFileDTO();
            postFileDTO.setId(fileVO.getId());
            postFileDTO.setFileName(fileVO.getFileName());
            postFileDTO.setFilePath(fileVO.getFilePath());
            postFileDTO.setFileType(fileVO.getFileType());
            postFileDTO.setFileSize(fileVO.getFileSize());
            postFileDTOList.add(postFileDTO);
        }

        return postFileDTOList;
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
