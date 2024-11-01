package com.app.ggumteo.service.inquiry;


import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class InquiryServiceImpl implements InquiryService {

    private final InquiryDAO inquiryDAO;

    @Override
    public void writeInquiry(PostDTO postDTO) {
        inquiryDAO.insertInquiry(postDTO);

        Long lastInsertId = inquiryDAO.getLastInsertId();
        postDTO.setId(lastInsertId);
        inquiryDAO.insertInquiryToTblInquiry(postDTO);

        log.info("문의사항 작성 완료, ID: {}", lastInsertId);
    }

    @Override
    public List<InquiryDTO> getInquiries(AdminPagination pagination) {
        return inquiryDAO.selectAll(pagination);
    }

    @Override
    public int getTotalInquiries() {
        return inquiryDAO.countTotal();
    }

    @Override
    public Map<String, Object> registerAnswer(Long inquiryId, String answerContent ,String answerDate) {
        inquiryDAO.updateInquiryStatus(inquiryId);
        return inquiryDAO.insertAdminAnswer(inquiryId, answerContent,answerDate);
    }
}



