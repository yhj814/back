package com.app.ggumteo.repository.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.mapper.admin.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminDAO {
    private final AdminMapper adminMapper;

    // 인증번호 조회
    public AdminVO getAdminVerifyCode(String adminVerifyCode) {
        return adminMapper.selectAdminVerifyCode(adminVerifyCode);
    }

    // 회원 정보 조회
    public List<MemberProfileDTO> getMembers() {
        return adminMapper.selectMembers();
    }
}

