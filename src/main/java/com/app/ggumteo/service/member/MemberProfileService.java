package com.app.ggumteo.service.member;


import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;

public interface MemberProfileService {
    //  내 정보 작성
    void write(MemberProfileDTO memberProfileDTO);
}
