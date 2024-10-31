package com.app.ggumteo.controller.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의사항 작성 페이지로 이동
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        log.info("문의사항 작성 페이지에 접근하였습니다.");

        // 초기 페이지에 빈 PostDTO 객체 전달
        model.addAttribute("postDTO", new PostDTO());

        return "inquiry/write"; // 문의사항 작성 페이지 템플릿 경로
    }

    // 문의사항 제출
    @PostMapping("/write")
    public RedirectView submitInquiry(@RequestParam String postTitle,
                                      @RequestParam String postContent,
                                      @RequestParam Long memberProfileId,
                                Model model) {
        log.info("문의사항 제출 요청 - 제목: {}, 내용: {}", postTitle, postContent);

        // PostDTO 객체에 제출된 데이터 설정
        PostDTO postDTO = new PostDTO();
        postDTO.setPostTitle(postTitle);
        postDTO.setPostContent(postContent);
        postDTO.setPostType("INQUIRY"); // 문의사항 타입 설정
        postDTO.setMemberProfileId(memberProfileId);

        // 문의사항 작성 처리
        inquiryService.writeInquiry(postDTO);
        log.info("문의사항이 성공적으로 저장되었습니다.");

        model.addAttribute("message", "문의사항이 성공적으로 작성되었습니다.");
        // 메인페이지로 이동
        return new RedirectView("/main");
    }

}

