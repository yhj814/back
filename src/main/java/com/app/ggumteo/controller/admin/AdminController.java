package com.app.ggumteo.controller.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    // 인증번호 입력 페이지
    @GetMapping("/verify")
    public String showVerifyPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO()); // DTO 객체를 모델에 추가
        return "admin/verify-code"; // 템플릿 이름
    }


    // 인증번호 확인
    @PostMapping("/verify")
    public RedirectView verifyAdminCode(@RequestParam("adminVerifyCode") String adminVerifyCode, RedirectAttributes redirectAttributes) {
        try {
            // String을 int로 변환
            int code = Integer.parseInt(adminVerifyCode);

            boolean isVerified = adminService.verifyAdminCode(String.valueOf(code)); // 변환된 값을 사용

            if (isVerified) {
                return new RedirectView("/admin/admin"); // 관리자 페이지로 리다이렉트
            } else {
                redirectAttributes.addFlashAttribute("error", "인증번호가 잘못되었습니다.");
                return new RedirectView("/admin/verify"); // 인증 실패 시 다시 인증 페이지로 리다이렉트
            }
        } catch (NumberFormatException e) {
            // 숫자가 아닌 경우 처리
            redirectAttributes.addFlashAttribute("error", "인증번호는 숫자여야 합니다.");
            return new RedirectView("/admin/verify");
        }
    }

}
