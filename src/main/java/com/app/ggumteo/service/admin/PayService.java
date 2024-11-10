package com.app.ggumteo.service.admin;

import com.app.ggumteo.domain.admin.PayWorkDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface PayService {
    //  작품 결제 목록
    List<PayWorkDTO> getWorkProducts(String search, AdminPagination pagination);

    // 작품 결제 목록 총 개수
    int getWorkProductCounts(String search);
}
