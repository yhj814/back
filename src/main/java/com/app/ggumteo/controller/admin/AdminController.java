package com.app.ggumteo.controller.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.domain.admin.AnnouncementVO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.service.admin.AdminService;
import com.app.ggumteo.service.admin.AnnouncementService;
import com.app.ggumteo.service.inquiry.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final AnnouncementService announcementService;

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
            response.put("redirect", "/admin/admin");
        } else {
            log.info("인증실패 !!");
            response.put("success", false);
        }

        return response;
    }

    // 관리자 페이지
    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin/admin";
    }

    // 공지사항 작성
    @PostMapping("/announcement")
    public ResponseEntity<String> writeAnnouncement(@RequestBody AnnouncementVO announcementVO) {
        announcementService.write(announcementVO);
        return ResponseEntity.ok("공지사항이 성공적으로 등록되었습니다.");
    }

    // 공지사항 목록 조회
    @GetMapping("/announcements")
    public String listAnnouncements(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model) {
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);
        pagination.setTotal(announcementService.getTotalAnnouncements());
        pagination.progress();

        List<AnnouncementVO> announcements = announcementService.getAllAnnouncements(pagination);

        log.info("Announcements List:");
        announcements.forEach(announcement -> log.info("{}", announcement));
        log.info("Pagination Details - Current Page: {}, Start Row: {}, End Row: {}, Total: {}",
                pagination.getPage(), pagination.getStartRow(), pagination.getEndRow(), pagination.getTotal());

        model.addAttribute("announcements", announcements);
        model.addAttribute("pagination", pagination);

        return "admin/admin"; // 공지사항 목록 페이지
    }

    @GetMapping("/announcements/list")
    public String getAnnouncementsFragment(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model) {
        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);
        pagination.setTotal(announcementService.getTotalAnnouncements());
        pagination.progress();

        List<AnnouncementVO> announcements = announcementService.getAllAnnouncements(pagination);
        model.addAttribute("announcements", announcements);
        model.addAttribute("pagination", pagination);

        return "admin/admin :: #announcement-list"; // 공지사항 목록만 반환
    }


//    @GetMapping("/inquiry")
//    public String getList(@ModelAttribute AdminPagination pagination, Model model) {
//        List<InquiryDTO> inquiryList = inquiryService.getList(pagination);
//        int totalInquiries = inquiryService.getTotal();
//
//        pagination.setTotal(totalInquiries); // 전체 문의 수 설정
//        pagination.progress(); // 페이지네이션 진행
//
//        log.info("Inquiry List: {}", inquiryList);
//        log.info("Pagination: {}", pagination);
//
//        model.addAttribute("inquiries", inquiryList);
//        model.addAttribute("pagination", pagination);
//
//        return "admin/admin"; // 템플릿 경로
//    }
}