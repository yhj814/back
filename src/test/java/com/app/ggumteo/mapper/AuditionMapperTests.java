package com.app.ggumteo.mapper;

import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.audition.AuditionVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.audition.AuditionMapper;
import com.app.ggumteo.mapper.post.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Random;

import java.util.Random;


@SpringBootTest
@Slf4j
public class AuditionMapperTests {

    @Autowired
    private AuditionMapper auditionMapper;

    @Autowired
    private PostMapper postMapper;



    @Test
    public void testInsertMultiple() {
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            // post 테이블에 데이터 삽입
            PostVO postVO = new PostVO();
            postVO.setPostTitle("모집TEST " + i);
            postVO.setPostContent("모집내용 TEST입니다. " + i + "번째 모집내용입니다.");
            postVO.setPostType("VIDEO");
            postVO.setMemberProfileId(19L); // memberProfileId는 고정값으로 설정

            postMapper.insert(postVO);

            Long postId = postVO.getId();
            if (postId == null) {
                log.info("postId is null for iteration: " + i);
                continue;
            }

            // audition 테이블에 데이터 삽입 (postId 사용)
            AuditionDTO auditionDTO = new AuditionDTO();
            auditionDTO.setId(postId);

            // AuditionField 값이 1~4 중 랜덤하게 설정
            auditionDTO.setAuditionField(random.nextInt(4) + 1);

            auditionDTO.setAuditionCareer("경력무관");
            auditionDTO.setExpectedAmount("100000/일");
            auditionDTO.setServiceStartDate("2024.11.04");
            auditionDTO.setAuditionDeadLine("2024.11.24");
            auditionDTO.setAuditionPersonnel(String.valueOf(4 + i)); // 예시로 인원수를 증가시킴
            auditionDTO.setAuditionLocation("서울특별시 강남구 역삼역 3번출구");
            auditionDTO.setAuditionBackground("TEST입니다. " + i + "번째 오디션 배경");
            auditionDTO.setAuditionCategory("단편영화 배우");

            // AuditionStatus 값이 "모집중" 또는 "모집완료" 중 랜덤하게 설정
            auditionDTO.setAuditionStatus(random.nextBoolean() ? "모집중" : "모집완료");

            AuditionVO auditionVO = auditionDTO.toVO();
            auditionMapper.insert(auditionVO);
        }
    }


}
