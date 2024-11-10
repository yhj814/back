package com.app.ggumteo.service.admin;

import com.app.ggumteo.domain.admin.PayWorkDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.admin.PayDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {
    private final PayDAO payDAO;

    //  작품 결제 목록
    @Override
    public List<PayWorkDTO> getWorkProducts(String search, AdminPagination pagination){
        return payDAO.getWorkProducts(search, pagination);
    }

    //  작품 결제 목록 총 개수
    @Override
    public int getWorkProductCounts(String search){
        return payDAO.getWorkProductCounts(search);
    }
}
