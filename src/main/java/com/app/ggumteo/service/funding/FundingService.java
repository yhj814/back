package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FundingService {
    // 펀딩 작성
    void write(FundingDTO fundingDTO, MultipartFile[] fundingFiles, MultipartFile thumbnailFile);
}
