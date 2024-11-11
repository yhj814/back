package com.app.ggumteo.repository.admin;

import com.app.ggumteo.domain.admin.PayFundingDTO;
import com.app.ggumteo.domain.admin.PayWorkDTO;
import com.app.ggumteo.mapper.admin.PayMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PayDAO {
    private final PayMapper payMapper;
    //  작품 결제 목록
    public List<PayWorkDTO> getWorkProducts(String search, AdminPagination pagination){
        return payMapper.selectWorkProducts(search, pagination);
    }

    //  작품 결제 목록 총 개수
    public int getWorkProductCounts(String search){
        return payMapper.workProductCounts(search);
    }

    //  펀딩 상품 결제 목록
    public List<PayFundingDTO> getFundingProducts(String search, AdminPagination pagination){
        return payMapper.selectFundingProducts(search, pagination);
    }

    //  펀딩 상품 결제 목록 총 개수
    public int getFundingProductCounts(String search){
        return payMapper.fundingProductCounts(search);
    }
}
