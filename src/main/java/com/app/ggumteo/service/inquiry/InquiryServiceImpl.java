package com.app.ggumteo.service.inquiry;


import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InquiryServiceImpl implements InquiryService {

    private final InquiryDAO inquiryDAO; // final로 선언하여 주입받도록 설정

    @Override
    public void writeInquiry(PostDTO postDTO) {
        // tbl_post에 문의사항을 삽입
        inquiryDAO.insertInquiry(postDTO);

        // 마지막으로 삽입된 ID를 가져와서 PostDTO에 세팅
        Long lastInsertId = inquiryDAO.getLastInsertId();
        postDTO.setId(lastInsertId); // 가져온 ID를 PostDTO에 설정

        // tbl_inquiry에 삽입
        inquiryDAO.insertInquiryToTblInquiry(postDTO);
    }

    @Override
    public List<InquiryDTO> getList(AdminPagination pagination) {
        return inquiryDAO.findAllInquiry(pagination);
    }

    @Override
    public int getTotal(){
        return inquiryDAO.getTotalInquiry();
    }
}
