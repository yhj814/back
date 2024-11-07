package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface AuditionApplicationFileService {
    void saveAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO);
    AuditionApplicationFileDTO saveAuditionApplicationFile(MultipartFile file, Long auditionApplicationId);

}
