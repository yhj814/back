package com.app.ggumteo.service.inquiry;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.InquiryVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.mapper.inquiry.InquiryMapper;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.repository.post.PostDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InquiryServiceImpl implements InquiryService {

    private final PostDAO postDAO;
    private final InquiryDAO inquiryDAO;

    @Override
    public void createInquiry(InquiryDTO inquiryDTO, Long memberId) {
        // 게시물 생성
        PostVO post = new PostVO();
        post.setPostTitle(inquiryDTO.getTitle());
        post.setPostContent(inquiryDTO.getDescription());
        // 문의하기 type
        post.setPostType("INQUIRY");
        post.setMemberId(memberId);
        postDAO.saveInquiry(post);

        // 마지막에 생성된 게시물 ID를 가져와서 문의 생성
        Long postId = postDAO.getLastInsertId();
        InquiryVO inquiry = new InquiryVO();
        inquiry.setPostId(postId);
        inquiry.setInquiryStatus("0"); // 미답변 상태
        inquiryDAO.saveInquiry(inquiry);
    }

}