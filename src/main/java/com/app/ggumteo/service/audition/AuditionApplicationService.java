package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import org.springframework.web.multipart.MultipartFile;

public interface AuditionApplicationService {

    void write(AuditionApplicationVO auditionApplicationVO, MultipartFile auditionApplicationFile);


}
