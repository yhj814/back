package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostFileService {
    void savePostFile(PostFileVO postFileVO);
    FileVO saveFile(MultipartFile file);
    byte[] getFileData(String fileName);  // 파일 데이터를 가져오는 메서드

    List<PostFileDTO> uploadFile(List<MultipartFile> file) throws IOException;

    // 파일 삭제
    void deleteFilesByIds(List<Long> fileIds);

    // 특정 게시물의 파일 조회 (반환 타입을 List<PostFileDTO>로 변경)
    List<PostFileDTO> findFilesByPostId(Long postId);
}