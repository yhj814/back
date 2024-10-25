package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface PostFileService {
    void savePostFile(PostFileVO postFileVO);
    // MultipartFile을 받아서 FileVO를 반환하는 메서드
    FileVO saveFile(MultipartFile file);
}