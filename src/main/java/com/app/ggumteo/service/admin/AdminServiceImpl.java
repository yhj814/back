package com.app.ggumteo.service.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.mapper.admin.AdminMapper;
import com.app.ggumteo.repository.admin.AdminDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO;

    @Override
    public void joinAdmin(AdminDTO adminDTO) {
        // DTO를 VO로 변환 후 DB에 삽입
        adminDAO.insertCode(adminDTO.toVO());
    }

    @Override
    public boolean verifyAdminCode(String adminVerifyCode) {
        // 입력한 인증번호로 DB에서 조회
        AdminVO adminVO = adminDAO.getAdminVerifyCode(adminVerifyCode);
        return adminVO != null; // 인증번호가 있으면 true 반환
    }
}

