package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class InquiryMapperTests {
    @Autowired
    private InquiryMapper inquiryMapper;

    @Test
    public void testInquiryMapper() {
        InquiryDTO inquiryDTO = new InquiryDTO();
        AdminPagination adminPagination = new AdminPagination();
        inquiryDTO.setPostId(8L);

    }
}
