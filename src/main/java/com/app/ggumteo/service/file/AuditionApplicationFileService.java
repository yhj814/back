package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuditionApplicationFileService {
    void saveAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO);
    FileVO saveFile(MultipartFile multipartFile, Long auditionApplicationId);
    List<AuditionApplicationFileDTO> uploadFile(List<MultipartFile> files) throws IOException;

}
