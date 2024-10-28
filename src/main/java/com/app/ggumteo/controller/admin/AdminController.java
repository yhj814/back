package com.app.ggumteo.controller.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.service.admin.AdminService;
import com.app.ggumteo.service.inquiry.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final InquiryService inquiryService;

    // 인증번호 입력 페이지
    @GetMapping("/verify")
    public String showVerifyPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO());
        return "admin/verify-code";
    }

    // 인증완료시 관리자 페이지로 이동
    @PostMapping("/verify")
    @ResponseBody
    public Map<String, Object> verifyCode(@ModelAttribute AdminDTO adminDTO) {
        Map<String, Object> response = new HashMap<>();
        boolean isVerified = adminService.verifyAdminCode(adminDTO.getAdminVerifyCode());
        if (isVerified) {
            response.put("success", true);
            log.info("인증번호 : {} 관리자 인증성공!! ", adminDTO.getAdminVerifyCode());
            // 리다이렉트
            response.put("redirect", "/admin/admin");
        } else {
            log.info("인증실패 !!");
            response.put("success", false);
        }
        // JSON 형태로 반환
        return response;
    }

    @GetMapping("/inquiry")
    public String getList(@ModelAttribute AdminPagination pagination, Model model) {
        List<InquiryDTO> inquiryList = inquiryService.getList(pagination);
        int totalInquiries = inquiryService.getTotal();

        pagination.setTotal(totalInquiries); // 전체 문의 수 설정
        pagination.progress(); // 페이지네이션 진행

        log.info("Inquiry List: {}", inquiryList);
        log.info("Pagination: {}", pagination);

        model.addAttribute("inquiries", inquiryList);
        model.addAttribute("pagination", pagination);

        return "admin/admin";
    }

}