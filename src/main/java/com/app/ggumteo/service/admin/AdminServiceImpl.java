package com.app.ggumteo.service.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.pagination.AdminPagination;
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
    public List<MemberProfileDTO> getMembers(String search, String order, AdminPagination pagination) {
        // 회원 정보 조회 (검색, 정렬, 페이지네이션 포함)
        return adminDAO.getMembers(search, order, pagination);
    }

    @Override
    public int getTotalMemberCount(String search, String order) {
        // 조건에 맞는 총 회원 수 조회
        return adminDAO.getTotalMemberCount(search, order);
    }

    @Override
    public int updateMemberStatus(Long memberId, String status) {
        // 회원 상태 변경
        return adminDAO.updateMemberStatus(memberId, status);
    }

    @Override
    public void deleteMembersByIds(List<Long> memberIds) {
        // 회원 프로필 삭제
        adminDAO.deleteMemberProfilesByMemberIds(memberIds);
        // 회원 정보 삭제
        adminDAO.deleteMembersByIds(memberIds);
    }
}


