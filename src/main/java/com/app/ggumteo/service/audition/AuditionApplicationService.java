package com.app.ggumteo.service.audition;

import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import org.springframework.web.multipart.MultipartFile;

public interface AuditionApplicationService {

    void write(AuditionApplicationDTO auditionApplicationDTO);

    int countApplicantsByAuditionId(Long auditionId);
}
