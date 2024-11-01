package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface PostFileService {
    void savePostFile(PostFileVO postFileVO);
    FileVO saveFile(MultipartFile file, Long postId);
    byte[] getFileData(String fileName);  // 파일 데이터를 가져오는 메서드
}