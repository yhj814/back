package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuditionApplicationFileService {
    void saveAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO);
    FileVO saveFile(MultipartFile multipartFile);
    byte[] getFileData(String fileName);  // 파일 데이터를 가져오는 메서드

    List<AuditionApplicationFileDTO> uploadFile(List<MultipartFile> files) throws IOException;

    //파일삭제
    void deleteAuditionApplicationFile(List<Long> fileIds);

    // 특정 게시물의 파일 조회 (반환 타입을 List<PostFileDTO>로 변경)
    List<AuditionApplicationFileDTO> findFilesByAuditionApplicationId(Long auditionApplicationId);

}
