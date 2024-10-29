package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PostFileServiceImpl implements PostFileService {

    private final FileDAO fileDAO;  // 파일 정보 저장용
    private final PostFileDAO postFileDAO;  // 파일과 게시물 관계 저장용

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        // 게시물과 파일의 관계를 tbl_post_file에 저장
        postFileDAO.insertPostFile(postFileVO);
    }

    @Override
    public FileVO saveFile(MultipartFile file, Long postId) {
        // 파일 정보를 담을 객체 생성
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());

        // 상대 경로를 생성하여 파일 저장 위치 설정
        String relativePath = "uploads/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        fileVO.setFilePath(relativePath);  // DB에 상대 경로만 저장

        // 실제 파일을 절대 경로에 저장 (예: C:/ggumteofile/uploads/)
        String rootPath = "C:/ggumteofile/";  // 실제 파일 저장 경로
        File saveLocation = new File(rootPath + relativePath);
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);  // 실제 파일을 해당 경로에 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // tbl_file에 파일 정보 저장
        fileDAO.save(fileVO);  // 파일 정보를 tbl_file 테이블에 저장하고, 자동 생성된 ID를 가져옴

        // tbl_post_file에 파일과 게시물의 관계 저장 (PostFileDAO 사용)
        PostFileVO postFileVO = new PostFileVO(fileVO.getId(), postId);  // 파일 ID와 게시물 ID로 관계 생성
        postFileDAO.insertPostFile(postFileVO);  // tbl_post_file 테이블에 파일-게시물 관계 저장

        return fileVO;
    }

}
