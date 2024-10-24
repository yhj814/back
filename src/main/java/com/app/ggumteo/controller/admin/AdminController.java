package com.app.ggumteo.controller.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    // 인증번호 입력 페이지
    @GetMapping("/verify")
    public String showVerifyPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO()); // DTO 객체를 모델에 추가
        return "admin/verify-code"; // 템플릿 이름
    }

//    @PostMapping("/verify")
//    @ResponseBody
//    public Map<String, Object> verifyAdminCode(@RequestParam("adminVerifyCode") String adminVerifyCode) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            // String을 int로 변환
//            int code = Integer.parseInt(adminVerifyCode);
//
//            // 변환된 값을 사용
//            boolean isVerified = adminService.verifyAdminCode(String.valueOf(code));
//
//            if (isVerified) {
//                log.info("관리자 인증 성공!!");
//
//                // 리다이렉트
//                RedirectView redirectView = new RedirectView();
//                redirectView.setUrl("/admin");
//                response.put("redirect", redirectView.getUrl());
//                return response;
//            } else {
//                log.info("인증번호가 잘못되었습니다.");
//                response.put("message", "인증번호가 잘못되었습니다.");
//            }
//        } catch (NumberFormatException e) {
//            log.info("인증번호는 숫자입니다.");
//            response.put("message", "인증번호는 숫자이어야 합니다.");
//        }
//
//        // 인증 실패 시 JSON 응답
//        return response;
//    }
@PostMapping("/verify")
@ResponseBody
public Map<String, Object> verifyCode(@ModelAttribute AdminDTO adminDTO) {
    Map<String, Object> response = new HashMap<>();

    boolean isVerified = adminService.verifyAdminCode(adminDTO.getAdminVerifyCode());
    if (isVerified) {
        response.put("success", true);
        log.info("인증번호 : {} 관리자 인증성공!! ", adminDTO.getAdminVerifyCode());
        response.put("redirect", "/admin/admin"); // 리다이렉트 URL 포함
    } else {
        log.info("인증실패 !!");
        response.put("success", false);
    }

    return response; // JSON 형태로 반환
}


}