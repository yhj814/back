package com.app.ggumteo.service.funding;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FundingService {
    // 펀딩 작성
    void write(FundingDTO fundingDTO, MultipartFile[] fundingFiles, MultipartFile thumbnailFile);

    List<FundingDTO> findFundingList(Search search, Pagination pagination);

    int findTotalWithSearchAndType(Search search);

    void updateFundingStatusToEnded();
}
