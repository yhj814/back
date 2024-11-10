package com.app.ggumteo.mapper.admin;

import com.app.ggumteo.domain.admin.PayFundingDTO;
import com.app.ggumteo.domain.admin.PayWorkDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayMapper {
//  작품 결제 목록
    List<PayWorkDTO> selectWorkProducts(
            @Param("search") String search,
            @Param("pagination") AdminPagination pagination
    );

//   작품 결제 목록 총 개수
    int workProductCounts(
            @Param("search") String search

    );

    //  펀딩 상품 결제 목록
    List<PayFundingDTO> selectFundingProducts(
            @Param("search") String search,
            @Param("pagination") AdminPagination pagination
    );

    //   펀딩 상품 결제 목록 총 개수
    int fundingProductCounts(
            @Param("search") String search

    );
}
