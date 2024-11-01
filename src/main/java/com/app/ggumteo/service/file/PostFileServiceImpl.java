package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
}
