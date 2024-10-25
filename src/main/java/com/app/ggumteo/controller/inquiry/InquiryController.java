package com.app.ggumteo.controller.inquiry;

import com.app.ggumteo.domain.inquiry.InquiryDTO;
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

    // 문의 작성 페이지
    @GetMapping("/write")
    public String showInquiryForm(Model model) {
        model.addAttribute("inquiryDTO", new InquiryDTO());
        return "inquiry/write";
    }

    // 문의 작성 처리
    @PostMapping("/write")
    public RedirectView writeInquiry(@ModelAttribute InquiryDTO inquiryDTO) {
        Long memberId = 1L; // 현재 로그인한 회원의 ID ,추후에 로그인 한 ID 가져오기
        inquiryService.createInquiry(inquiryDTO, memberId); // 문의사항 생성

        log.info("문의사항이 작성되었습니다: {}", inquiryDTO.getTitle()); // 작성한 제목
        // 관리자 페이지로 리다이렉트
        // 추후에 이동할페이지로 수정
        return new RedirectView("/admin/admin");
    }
}
