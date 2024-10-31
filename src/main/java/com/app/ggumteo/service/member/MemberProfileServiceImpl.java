package com.app.ggumteo.service.member;

import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.repository.member.MemberProfileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileDAO memberProfileDAO;

    @Override
    public MemberProfileDTO write(MemberProfileDTO memberProfileDTO) {
        MemberProfileVO memberProfileVO = new MemberProfileVO();
        memberProfileVO.setProfileName(memberProfileDTO.getProfileName());
        memberProfileVO.setProfileNickName(memberProfileDTO.getProfileNickName());
        memberProfileVO.setProfileGender(memberProfileDTO.getProfileGender());
        memberProfileVO.setProfileAge(memberProfileDTO.getProfileAge());
        memberProfileVO.setProfileEmail(memberProfileDTO.getProfileEmail());
        memberProfileVO.setProfilePhone(memberProfileDTO.getProfilePhone());
        memberProfileVO.setProfileEtc(memberProfileDTO.getProfileEtc());
        memberProfileVO.setMemberId(memberProfileDTO.getMemberId());

        // 저장 후 자동 생성된 ID를 설정
        memberProfileDAO.save(memberProfileVO);
        memberProfileDTO.setId(memberProfileVO.getId());  // 생성된 ID를 DTO에 설정하여 반환

        return memberProfileDTO;  // DTO 객체 반환
    }
}
