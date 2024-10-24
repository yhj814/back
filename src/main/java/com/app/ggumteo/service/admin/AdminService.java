package com.app.ggumteo.service.admin;

import com.app.ggumteo.domain.admin.AdminDTO;

public interface AdminService {
    // 관리자 인증번호 삽입
    public void joinAdmin(AdminDTO adminDTO);

    // 관리자 인증번호 확인
    public boolean verifyAdminCode(String adminVerifyCode);
}

