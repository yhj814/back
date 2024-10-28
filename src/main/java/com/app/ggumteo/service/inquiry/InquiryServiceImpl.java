package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InquiryServiceImpl implements InquiryService {
    private final InquiryDAO inquiryDAO;

    @Override
    public void createInquiry(InquiryDTO inquiryDTO, Long memberId) {
        // memberId에 따라 postId를 가져오는 로직이 필요합니다.
        Long postId = getPostIdForMember(memberId); // 현재 회원의 게시물 ID를 가져오는 메서드 필요
        inquiryDTO.setPostId(postId);
        inquiryDAO.insertInquiry(inquiryDTO);
    }

    private Long getPostIdForMember(Long memberId) {
        // 구현할 로직: memberId에 따라 적절한 postId를 찾는 로직 필요
        return 1L; // 예시로 1L을 반환
    }
}