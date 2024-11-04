package com.app.ggumteo.mapper.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.pagination.AdminPagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
//    인증번호 생성
    public void insert(AdminVO adminVO);

//    인증번호 조회
    public AdminVO selectAdminVerifyCode(String adminVerifyCode);


//   회원 조회 (검색, 필터, 페이지네이션 포함)
    List<MemberProfileDTO> selectMembers(
            @Param("search") String search,
            @Param("order") String order,
            @Param("pagination") AdminPagination pagination
    );


//   총 회원 수 조회 (검색 및 필터 조건 적용)
    int countTotalMembers(
            @Param("search") String search,
            @Param("order") String order
    );

//   회원 상태 변경
    int updateMemberStatus(
            @Param("memberId") Long memberId,
            @Param("status") String status
    );

//   회원 삭제
    int deleteMembersByIds(
            @Param("memberIds") List<Long> memberIds
    );

//  회원 프로필 삭제
    int deleteMemberProfilesByMemberIds(
            @Param("memberIds") List<Long> memberIds
    );
}