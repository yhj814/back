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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PostFileServiceImpl implements PostFileService {

    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        postFileDAO.insertPostFile(postFileVO);
    }

    @Override
    public FileVO saveFile(MultipartFile file, Long postId) {
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());

        String relativePath = "uploads/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        fileVO.setFilePath(relativePath);

        String rootPath = "C:/ggumteofile/";
        File saveLocation = new File(rootPath + relativePath);
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        fileDAO.save(fileVO);
        PostFileVO postFileVO = new PostFileVO(fileVO.getId(), postId);
        postFileDAO.insertPostFile(postFileVO);

        return fileVO;
    }

    @Override
    public byte[] getFileData(String fileName) {
        String rootPath = "C:/ggumteofile/";
        File file = new File(rootPath + fileName);
        try {
            if (file.exists()) {
                return Files.readAllBytes(file.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
        return null;  // 파일이 없으면 null 반환
    }

    // 파일 올리자마자 갖고오기
    public PostFileDTO uploadFile(MultipartFile file) throws IOException {
        // 파일 저장 경로 설정
        String rootPath = "C:/upload/" + getPath();
        UUID uuid = UUID.randomUUID();
        String uniqueFileName = uuid.toString() + "_" + file.getOriginalFilename();
        String relativePath = getPath() + "/" + uniqueFileName;

        // 파일 정보 설정
        PostFileDTO postFileDTO = new PostFileDTO();
        postFileDTO.setFileName(uniqueFileName);
        postFileDTO.setFilePath(relativePath);
        postFileDTO.setFileType(file.getContentType());
        postFileDTO.setFileSize(String.valueOf(file.getSize()));

        File saveLocation = new File(rootPath, uniqueFileName);
        if (!saveLocation.getParentFile().exists()) {
            saveLocation.getParentFile().mkdirs();
        }
        file.transferTo(saveLocation);

        // 이미지 파일일 경우 썸네일 생성
        if (file.getContentType() != null && file.getContentType().startsWith("image")) {
            try (FileOutputStream thumbnail = new FileOutputStream(new File(rootPath, "t_" + uniqueFileName))) {
                Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
            }
        }
        return postFileDTO;
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
