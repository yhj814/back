package com.app.ggumteo.service.admin;


import com.app.ggumteo.domain.member.MemberProfileDTO;

import java.util.List;

public interface AdminService {

    // 관리자 인증번호 확인
    public boolean verifyAdminCode(String adminVerifyCode);

    // 회원 정보 조회
    List<MemberProfileDTO> getMembers();
}

