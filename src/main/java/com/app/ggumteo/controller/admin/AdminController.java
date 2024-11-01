package com.app.ggumteo.controller.admin;

import com.app.ggumteo.domain.admin.AdminDTO;
import com.app.ggumteo.domain.admin.AnnouncementVO;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.pagination.AdminPagination;
import com.app.ggumteo.service.admin.AdminService;
import com.app.ggumteo.service.admin.AnnouncementService;
import com.app.ggumteo.service.inquiry.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    // 인증 완료 시 관리자 페이지로 이동
    @PostMapping("/verify")
    @ResponseBody
    public Map<String, Object> verifyCode(@ModelAttribute AdminDTO adminDTO) {
        Map<String, Object> response = new HashMap<>();
        boolean isVerified = adminService.verifyAdminCode(adminDTO.getAdminVerifyCode());

        if (isVerified) {
            response.put("success", true);
            log.info("인증번호 {}로 관리자 인증 성공!", adminDTO.getAdminVerifyCode());
            response.put("redirect", "/admin/admin");
        } else {
            log.warn("인증 실패 - 입력된 인증번호: {}", adminDTO.getAdminVerifyCode());
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
        log.info("공지사항이 등록되었습니다.");
        return ResponseEntity.ok("공지사항이 성공적으로 등록되었습니다.");
    }

    // 공지사항 조회 (검색 및 정렬 기능 포함)
    @GetMapping("/announcements")
    @ResponseBody
    public Map<String, Object> listAnnouncements(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "order", defaultValue = "createdDate") String order,
            @RequestParam(value = "search", required = false) String search) {

        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);
        pagination.setTotal(announcementService.getTotalAnnouncements(search));
        pagination.progress();

        List<AnnouncementVO> announcements = announcementService.getAllAnnouncements(pagination, order, search);
        log.info("공지사항 조회 - 페이지: {}, 검색어: {}, 정렬 기준: {}", page, search, order);

        Map<String, Object> response = new HashMap<>();
        response.put("announcements", announcements);
        response.put("pagination", pagination);

        return response;
    }

    // 공지사항 수정
    @PostMapping("/announcement/update")
    @ResponseBody
    public ResponseEntity<String> updateAnnouncement(@RequestBody AnnouncementVO announcementVO) {
        announcementService.updateAnnouncement(announcementVO);
        log.info("공지사항이 수정되었습니다. ID: {}", announcementVO.getId());
        return ResponseEntity.ok("공지사항이 성공적으로 수정되었습니다.");
    }

    // 공지사항 삭제
    @PostMapping("/announcement/delete")
    @ResponseBody
    public ResponseEntity<String> deleteAnnouncements(@RequestBody List<Integer> ids) {
        announcementService.deleteAnnouncements(ids);
        log.info("선택한 공지사항이 삭제되었습니다. 삭제된 ID 목록: {}", ids);
        return ResponseEntity.ok("선택한 공지사항이 성공적으로 삭제되었습니다.");
    }

    // 정렬 및 검색어가 적용된 문의사항 목록 조회 (페이징 처리 포함)
    @GetMapping("/inquiries")
    @ResponseBody
    public Map<String, Object> listInquiries(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "order", required = false, defaultValue = "inquiry-created-date") String order,
            @RequestParam(value = "search", required = false) String search) {

        AdminPagination pagination = new AdminPagination();
        pagination.setPage(page);

        // 정렬 및 검색을 적용하여 총 문의사항 개수를 가져옴
        int totalInquiries = inquiryService.getTotalInquiries(order, search);
        pagination.setTotal(totalInquiries);
        pagination.progress();

        // 정렬과 검색어가 적용된 문의사항 목록을 가져옴
        List<InquiryDTO> inquiries = inquiryService.getInquiries(pagination, order, search);
        log.info("문의사항 조회 - 페이지: {}, 정렬 기준: {}, 검색어: {}, 문의사항 개수: {}", page, order, search, inquiries.size());

        Map<String, Object> response = new HashMap<>();
        response.put("inquiries", inquiries);
        response.put("pagination", pagination);

        return response;
    }

    // 문의사항 답변 등록
    @PostMapping("/inquiries/{inquiryId}/answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerAnswer(
            @PathVariable Long inquiryId,
            @RequestBody Map<String, String> requestData) {

        String answerContent = requestData.get("answerContent");
        Map<String, Object> answerInfo = inquiryService.registerAnswer(inquiryId, answerContent);

        log.info("문의 ID {} 답변 등록 완료. 답변 내용: {}", inquiryId, answerContent);
        return ResponseEntity.ok(answerInfo);
    }

    // 문의사항 삭제
    @PostMapping("/inquiry/delete")
    @ResponseBody
    public ResponseEntity<String> deleteInquiries(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 게시물을 선택하세요.");
        }

        log.info("삭제 요청 ID: {}", ids);  // 요청받은 ID

        try {
            inquiryService.deleteSelectedInquiries(ids);
            log.info("선택된 문의사항이 삭제되었습니다.");
            return ResponseEntity.ok("선택된 문의사항이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("문의사항 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문의사항 삭제 중 오류가 발생했습니다.");
        }
    }

    // 회원 정보 조회
    @GetMapping("/members")
    @ResponseBody
    public List<MemberProfileDTO> getMembers() {
        List<MemberProfileDTO> members = adminService.getMembers();
        log.info("회원 정보 조회 완료: {}", members);
        return members;
    }
}



