package com.app.ggumteo.mapper.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
//    인증번호 생성
    public void insert(AdminVO adminVO);

//    인증번호 조회
    public AdminVO selectAdminVerifyCode(String adminVerifyCode);

//    회원조희
    List<MemberProfileDTO> selectMembers();
}