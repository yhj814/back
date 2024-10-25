package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.repository.file.PostFileDAO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class PostFileServiceImpl implements PostFileService {

    @Autowired
    private PostFileDAO postFileDAO;

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        postFileDAO.insertPostFile(postFileVO); // 게시물과 파일의 관계 저장
    }

    @Override
    public FileVO saveFile(MultipartFile file, Long postId) {
        // 파일 정보를 담을 객체 생성
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath("C:/ggumteofile/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename());

        // 파일 저장 로직
        File saveLocation = new File(fileVO.getFilePath());
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
        // **tbl_file에 파일 정보 저장** (파일을 먼저 저장)
        postFileDAO.insertFile(fileVO); // fileVO의 id가 자동 생성되도록 설정되어 있어야 함

        // 파일 정보 저장 후, 파일과 게시물의 관계 저장
        postFileDAO.insertPostFile(new PostFileVO(fileVO.getId(), postId));  // tbl_post_file에 파일과 게시물 관계 저장

        return fileVO;  // 파일 정보 반환
    }
}
