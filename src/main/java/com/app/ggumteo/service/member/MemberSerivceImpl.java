package com.app.ggumteo.service.member;

import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.mapper.member.MemberMapper;
import com.app.ggumteo.repository.member.MemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberSerivceImpl implements MemberService {
    private final MemberDAO memberDAO;
    private final MemberMapper memberMapper;

//    memberVO로 카카오 로그인 처리
    @Override
    public MemberVO join(MemberVO memberVO) {
        // 카카오 로그인 처리(카카오 이메일로 회원 존재 여부 확인)
        Optional<MemberVO> foundKakaoMember = memberDAO.findByKakaoEmail(memberVO.getMemberEmail());

        if (foundKakaoMember.isEmpty()) {
            memberDAO.save(memberVO); // 저장 후 memberVO에 id가 세팅됨
        } else {
            // 이미 존재하는 경우, 기존 회원 정보를 반환
            memberVO = foundKakaoMember.get();
        }

        return memberVO; // id가 세팅된 memberVO 반환
    }

    @Override
    public Optional<MemberVO> getKakaoMember(String memberEmail) {
        return memberDAO.findByKakaoEmail(memberEmail);
    }

    // 로그인 시 프로필 이미지 업데이트
    @Override
    public void updateProfileImgUrl(String memberEmail, String profileImgUrl) {
        memberDAO.updateProfileImgUrl(memberEmail, profileImgUrl);
    }


}
