package com.app.ggumteo.service.admin;


import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.pagination.AdminPagination;

import java.util.List;

public interface AdminService {

    // 관리자 인증번호 확인
    public boolean verifyAdminCode(String adminVerifyCode);

    // 회원 정보 조회 (검색, 정렬, 페이지네이션 포함)
    List<MemberProfileDTO> getMembers(String search, String order, AdminPagination pagination);

    // 조건에 맞는 총 회원 수 조회
    int getTotalMemberCount(String search, String order);

    // 회원 상태 변경
    int updateMemberStatus(Long memberId, String status);

    // 회원 삭제 (회원 프로필 포함)
    void deleteMembersByIds(List<Long> memberIds);

}

