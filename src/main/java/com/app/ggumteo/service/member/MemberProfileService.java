package com.app.ggumteo.service.member;

import com.app.ggumteo.domain.member.MemberProfileDTO;

public interface MemberProfileService {
    // 내 정보 작성 후, 생성된 MemberProfileDTO 반환
    MemberProfileDTO write(MemberProfileDTO memberProfileDTO);
}
