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
    public void join(MemberVO memberVO) {
//       카카오 로그인 처리(카카오 이메일로 회원 존재 여부 확인)
        Optional<MemberVO> foundKakaoMember
                = memberDAO.findByKakaoEmail(memberVO.getMemberEmail());

        if (foundKakaoMember.isEmpty()){
            memberDAO.save(memberVO);
        }
    }

    @Override
    public Optional<MemberVO> getKakaoMember(String memberEmail) {
        return Optional.empty();
    }
}
