package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.mapper.admin.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j

public class AdminMapperTests {
    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void testInsert() {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminVerifyCode("111111");
        log.info("{}","관리자 인증번호 생성완료");
        adminMapper.insert(adminDTO.toVO());
    }
}
