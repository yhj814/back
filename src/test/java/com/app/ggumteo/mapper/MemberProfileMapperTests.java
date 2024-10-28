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
        Long memberId = 1L;

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
}
