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
        fileVO.setFileName(file.getOriginalFilename());  // 파일 원래 이름 설정
        fileVO.setFileSize(String.valueOf(file.getSize()));  // 파일 크기 설정
        fileVO.setFileType(file.getContentType());  // 파일 타입 설정
        fileVO.setFilePath("/path/to/save/" + file.getOriginalFilename());  // 파일 경로 설정

        // 실제로 파일을 서버 경로에 저장
        File saveLocation = new File(fileVO.getFilePath());
        try {
            file.transferTo(saveLocation);  // 파일을 지정된 경로에 저장
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장에 실패했습니다.");
        }

        // 파일 정보를 데이터베이스에 저장
        postFileDAO.insertFile(fileVO);

        // 파일 정보가 담긴 FileVO 객체를 반환
        return fileVO;
    }
}
