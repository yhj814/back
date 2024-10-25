package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.repository.file.PostFileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PostFileServiceImpl implements PostFileService {
    @Autowired
    private PostFileDAO postFileDAO;

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        postFileDAO.insertPostFile(postFileVO);
    }

    @Override
    public FileVO saveFile(MultipartFile file) {
        // 파일 정보를 담을 객체 생성
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());  // 파일 이름 설정
        fileVO.setFileSize(String.valueOf(file.getSize()));  // 파일 크기 설정
        fileVO.setFileType(file.getContentType());  // 파일 타입 설정
        fileVO.setFilePath("/path/to/save/" + file.getOriginalFilename());  // 파일 저장 경로 설정

        // 파일 저장 로직
        File saveLocation = new File(fileVO.getFilePath());
        try {
            file.transferTo(saveLocation);  // 파일을 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 데이터베이스에 파일 정보를 저장
        postFileDAO.insertFile(fileVO);

        // FileVO 반환
        return fileVO;
    }

}
