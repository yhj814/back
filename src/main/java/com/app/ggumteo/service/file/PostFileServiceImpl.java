package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
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
public class PostFileServiceImpl implements PostFileService {

    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;
    private final String rootPath = "C:/upload/";

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        postFileDAO.insertPostFile(postFileVO);
    }

    @Override
    public FileVO saveFile(MultipartFile file, Long postId) {
        String relativePath = getPath() + "/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        FileVO fileVO = new FileVO();
        fileVO.setFileName(relativePath);
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath(getPath());

        // 파일 저장 경로 설정
        File saveLocation = new File(rootPath + relativePath);
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);  // 파일 실제 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // DB에 파일 정보 저장
        fileDAO.save(fileVO);
        postFileDAO.insertPostFile(new PostFileVO(fileVO.getId(), postId));

        return fileVO;
    }


    @Override
    public FileVO saveFileRecord(String fileName, Long postId) {
        FileVO fileVO = new FileVO();
        fileVO.setFileName(fileName);
        fileVO.setFilePath(getPath());

        fileDAO.save(fileVO);
        postFileDAO.insertPostFile(new PostFileVO(fileVO.getId(), postId));

        return fileVO;
    }

    @Override
    public byte[] getFileData(String fileName) {
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
    public List<PostFileDTO> uploadFiles(List<MultipartFile> files) throws IOException {
        List<PostFileDTO> fileDTOs = new ArrayList<>();
        String directoryPath = rootPath + getPath();
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String uniqueFileName = uuid.toString() + "_" + file.getOriginalFilename();
            File saveLocation = new File(directoryPath, uniqueFileName);
            file.transferTo(saveLocation);

            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                try (FileOutputStream thumbnail = new FileOutputStream(new File(directoryPath, "t_" + uniqueFileName))) {
                    Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
                }
            }

            PostFileDTO postFileDTO = new PostFileDTO();
            postFileDTO.setFileName(uniqueFileName);
            postFileDTO.setFilePath(getPath());
            postFileDTO.setFileType(file.getContentType());
            postFileDTO.setFileSize(String.valueOf(file.getSize()));
            fileDTOs.add(postFileDTO);
        }
        return fileDTOs;
    }

    @Override
    public void deleteFilesByIds(List<Long> fileIds) {
        fileIds.forEach(fileId -> {
            postFileDAO.deletePostFileByFileId(fileId);
            fileDAO.deleteFile(fileId);
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
