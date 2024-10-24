package com.app.ggumteo.mapper.admin;

import com.app.ggumteo.domain.admin.AdminVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
//    인증번호 생성
    public void insert(AdminVO adminVO);

//    인증번호 조회
    public AdminVO selectAdminVerifyCode(String adminVerifyCode);
}