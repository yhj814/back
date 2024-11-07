package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostFileService {
    // 게시물과 파일의 관계 저장
    void savePostFile(PostFileVO postFileVO);

    FileVO saveFile(MultipartFile file, Long postId);

    // 업로드된 파일 기록을 DB에 저장 (파일 이름과 게시물 ID)
    FileVO saveFileRecord(String fileName, Long postId);

    // 업로드된 파일을 저장하고 썸네일 생성
    List<PostFileDTO> uploadFiles(List<MultipartFile> files) throws IOException;

    // 파일 데이터를 가져오는 메서드
    byte[] getFileData(String fileName);

    // 파일 ID로 여러 파일 삭제
    void deleteFilesByIds(List<Long> fileIds);

    // 특정 게시물의 파일 리스트 조회
    List<PostFileDTO> findFilesByPostId(Long postId);
}
