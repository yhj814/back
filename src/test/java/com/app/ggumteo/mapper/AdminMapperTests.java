package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.mapper.admin.AdminMapper;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.pagination.AdminPagination;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j

public class AdminMapperTests {
    @Autowired
    private AdminMapper adminMapper;
    private InquiryMapper inquiryMapper;

    @Test
    public void testInsert() {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminVerifyCode("111111");
        log.info("{}","관리자 인증번호 생성완료");
        adminMapper.insert(adminDTO.toVO());
    }

    @Test
    public void testSelectVerifyCode() {
        String verifyCode = "222222";

        AdminVO foundVerifyCode =adminMapper.selectAdminVerifyCode(verifyCode);

        if(foundVerifyCode != null) {
            log.info("{},{}",foundVerifyCode,"인증번호 있음");
        }
        else {
            log.info("{}","인증번호 없음");
        }
    }

}
