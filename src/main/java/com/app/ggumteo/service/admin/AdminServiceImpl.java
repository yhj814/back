package com.app.ggumteo.service.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.repository.admin.AdminDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO;

    @Override
    public boolean verifyAdminCode(String adminVerifyCode) {
        // 입력한 인증번호로 DB에서 조회
        AdminVO adminVO = adminDAO.getAdminVerifyCode(adminVerifyCode);
        // 인증번호가 있으면 true 반환
        return adminVO != null;
    }

    @Override
    public List<MemberProfileDTO> getMembers() {
        // 회원 정보 조회
        return adminDAO.getMembers();
    }
}

