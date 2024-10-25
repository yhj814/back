package com.app.ggumteo.service.admin;

public interface AdminService {

    // 관리자 인증번호 확인
    public boolean verifyAdminCode(String adminVerifyCode);
}

