package com.app.ggumteo.repository.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.mapper.admin.AdminMapper;
import com.app.ggumteo.pagination.AdminPagination;
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

    // 회원 정보 조회 (검색, 필터, 페이지네이션 포함)
    public List<MemberProfileDTO> getMembers(String search, String order, AdminPagination pagination) {
        return adminMapper.selectMembers(search, order, pagination);
    }

    // 조건에 맞는 총 회원 수 조회
    public int getTotalMemberCount(String search, String order) {
        return adminMapper.countTotalMembers(search, order);
    }

    // 회원 상태 변경
    public int updateMemberStatus(Long memberId, String status) {
        return adminMapper.updateMemberStatus(memberId, status);
    }

    // 회원 프로필 삭제
    public int deleteMemberProfilesByMemberIds(List<Long> memberIds) {
        return adminMapper.deleteMemberProfilesByMemberIds(memberIds);
    }

    // 회원 삭제
    public int deleteMembersByIds(List<Long> memberIds) {
        return adminMapper.deleteMembersByIds(memberIds);
    }
}

