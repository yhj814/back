package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.mapper.member.MemberProfileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberProfileMapperTests {

    @Autowired
    private MemberProfileMapper memberProfileMapper;

    @Test
    public void testInsert() {
        Long memberId = 9L;

        // tbl_member_profile에 삽입할 데이터 생성
        MemberProfileDTO memberProfileDTO = new MemberProfileDTO();
        memberProfileDTO.setProfileName("홍길동");
        memberProfileDTO.setProfileNickName("길동");
        memberProfileDTO.setProfileGender("남성");
        memberProfileDTO.setProfileAge(12);
        memberProfileDTO.setProfileEmail("gildong@example.com");
        memberProfileDTO.setProfilePhone("010-1234-5678");
        memberProfileDTO.setProfileEtc("추가 정보");
        memberProfileDTO.setMemberId(memberId);  // 참조하는 memberId 설정

        MemberProfileVO memberProfileVO = memberProfileDTO.toVO();

        memberProfileMapper.insert(memberProfileVO);

    }

    @Test
    public void testUpdateMemberProfile() {
        MemberProfileDTO memberProfileDTO = new MemberProfileDTO();
        memberProfileDTO.setId(2L);
        memberProfileDTO.setProfileName("수정된 이름");
        memberProfileDTO.setProfileNickName("수정된 닉네임");
        memberProfileDTO.setProfileAge(30);
        memberProfileDTO.setProfileGender("남성");
        memberProfileDTO.setProfileEmail("000test@gmail.com");
        memberProfileDTO.setProfilePhone("01045678912");
        memberProfileDTO.setProfileEtc("추가추가");

        memberProfileMapper.updateByMemberId(memberProfileDTO.toVO());

        log.info("memberProfileDTO={}", memberProfileDTO);
    }
}
